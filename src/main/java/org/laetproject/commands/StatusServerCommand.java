package org.laetproject.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.laetproject.Bot;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StatusServerCommand extends ListenerAdapter {

    private final Dotenv config = Dotenv.load();
    private final String CLIENT_ID_IMGBB = config.get("CLIENT_ID_IMGBB");
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase(Bot.PREFIX + "status")) {
            if (command.length == 2) {

                String url = "https://api.mcsrvstat.us/3/" + command[1];
                Request request = new Request.Builder().url(url).build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Erro na requisição");
                    }
                    String respondeBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(respondeBody);

                        String thumbnailUrl = null;
                        if(jsonObject.has("icon")) {
                            String base64String = jsonObject.getString("icon");
                            if (base64String.startsWith("data:image/png;base64,")) {
                                base64String = base64String.replace("data:image/png;base64,", "");
                            }

                            try {
                                thumbnailUrl = uploudImage(base64String);
                            } catch (IOException | InterruptedException | URISyntaxException e) {
                                e.printStackTrace();
                            }
                        }else
                            thumbnailUrl = "https://preview.redd.it/18i8xym9vlc51.png?auto=webp&s=4552a89660039061b215a5732f6f2fbc5d957ee9";

                        EmbedBuilder embedBuilder = new EmbedBuilder()
                                .setColor(new Color(0x0BFF00))
                                .setTitle(jsonObject.optString("hostname", "N/A"))
                                .setThumbnail(thumbnailUrl)
                                .addField("IP Numérico", jsonObject.optString("ip", "N/A"), false)
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

                        event.getMessage().replyEmbeds(embedBuilder.build()).queue();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String uploudImage(String base64String) throws IOException, InterruptedException, URISyntaxException {
        String url = "https://api.imgbb.com/1/upload?expiration=600&key=" + CLIENT_ID_IMGBB;

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", base64String)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JSONObject jsonObject = new JSONObject(response.body().string());
            return jsonObject.getJSONObject("data").getString("url");
        }

    }

}

