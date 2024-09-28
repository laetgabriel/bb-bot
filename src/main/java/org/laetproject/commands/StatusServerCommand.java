package org.laetproject.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.laetproject.Bot;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StatusServerCommand extends ListenerAdapter {

    private final Dotenv config = Dotenv.load();
    private final String CLIENT_ID_IMGUR = config.get("CLIENT_ID_IMGUR");

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase(Bot.PREFIX + "status")) {
            if (command.length == 2) {
                OkHttpClient client = new OkHttpClient();
                String url = "https://api.mcsrvstat.us/3/" + command[1];
                Request request = new Request.Builder().url(url).build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Erro na requisição");
                    }
                    String respondeBody = response.body().string();

                    event.getChannel().sendMessage("Carregando dados...").queue(message -> {
                        JSONObject jsonObject = new JSONObject(respondeBody);


                        if (jsonObject.getString("ip").equalsIgnoreCase("127.0.0.1")) {
                            message.editMessage("Não consegui encontrar esse servidor").queue();
                            return;
                        }
                        String base64String = jsonObject.getString("icon");
                        if (base64String.startsWith("data:image/png;base64,")) {
                            base64String = base64String.replace("data:image/png;base64,", "");
                        }
                        String thumbnailUrl = uploudImgur(base64String);

                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setColor(new Color(0x0BFF00))
                                .setTitle(jsonObject.optString("hostname", "N/A"))
                                .setThumbnail(thumbnailUrl != null ? thumbnailUrl : "N/A")
                                .addField("IP Número", jsonObject.optString("ip", "N/A"), false)
                                .addField("Porta", String.valueOf(jsonObject.getInt("port")), true)
                                .addField("Online", jsonObject.getBoolean("online") ? "✅ Sim" : "❌ Não", true)
                                .addField("Versão do Servidor", jsonObject.optString("version", "N/A"), true)
                                .setFooter("Solicitado por: " + event.getAuthor().getName(), event.getAuthor().getAvatarUrl());

                        if (jsonObject.has("players")) {
                            JSONObject players = jsonObject.getJSONObject("players");
                            int onlinePlayers = players.getInt("online");
                            int maxPlayers = players.getInt("max");
                            embedBuilder.addField("Jogadores Online", onlinePlayers + "/" + maxPlayers, true);
                        } else
                            embedBuilder.addField("Jogadores Online", "N/A", true);

                        if (jsonObject.has("motd")) {
                            JSONObject motd = jsonObject.getJSONObject("motd");
                            JSONArray rawArray = motd.getJSONArray("clean");
                            StringBuilder rawBuilder = new StringBuilder();

                            for (int i = 0; i < rawArray.length(); i++) {
                                rawBuilder.append(rawArray.getString(i));
                                if (i < rawArray.length() - 1) {
                                    rawBuilder.append("\n");
                                }
                            }
                            embedBuilder.addField("Motd", rawBuilder.toString(), true);
                        } else
                            embedBuilder.addField("Motd", "N/A", true);


                        message.editMessageEmbeds(embedBuilder.build()).setContent("").queue();

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String uploudImgur(String base64String) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.mcsrvstat.us/3/" + base64String;
        String attachmentUrl = null;
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", base64String)
                .build();

        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .post(body)
                .addHeader("Authorization", "Client-ID " + CLIENT_ID_IMGUR)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição");
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getJSONObject("data").getString("link");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
