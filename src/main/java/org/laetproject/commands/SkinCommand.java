package org.laetproject.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.laetproject.Bot;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SkinCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        OkHttpClient client = new OkHttpClient();
        String encoded;
        String url;
        if (event.getAuthor().isBot()) return;
        String[] command = event.getMessage().getContentRaw().split(" ");
        if (command[0].equalsIgnoreCase(Bot.PREFIX + "skin") && command.length > 1) {
            if (command[1].equalsIgnoreCase("pose") && command.length == 3) {

                encoded = URLEncoder.encode(command[2], StandardCharsets.UTF_8);
                url = "https://starlightskins.lunareclipse.studio/render/custom/" + encoded +
                        "/full?wideModel=https://cdn.lunareclipse.studio/model.obj&slimModel=https://cdn.lunareclipse.studio/model.obj&cameraPosition=" +
                        "{%22x%22:%22-4.94%22,%22y%22:%2235.00%22,%22z%22:%22-21.6%22}&cameraFocalPoint={%22x%22:%223.67%22,%22y%22:%2216.31%22,%22z%22:%223.35%22}";

                Request request = new Request.Builder().url(url).get().build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful())
                        throw new IOException("Erro inesperado " + response);

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**Skin Pose**")
                            .addField("Nome da conta:", "_" + command[2].toUpperCase() + "_", false)
                            .setImage(url);

                    event.getChannel().sendMessageEmbeds(embed.build()).queue();


                } catch (IOException e) {
                    erroMessage(event, "_Conta não encontrada_ 😿");
                }
            } else if (!command[1].isEmpty() && command.length == 2 && !command[1].equalsIgnoreCase("pose")) {
                encoded = URLEncoder.encode(command[1], StandardCharsets.UTF_8);
                url = "https://starlightskins.lunareclipse.studio/render/mojavatar/" + encoded + "/full";

                Request request = new Request.Builder().url(url).get().build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful())
                        throw new IOException("Erro inesperado " + response);

                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("**Skin Cabecao**")
                            .addField("Nome da conta:", "_" + command[1].toUpperCase() + "_", false)
                            .setThumbnail(url);

                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                } catch (IOException e) {
                    erroMessage(event, "_Conta não encontrada_ 😿");
                }
            }
        }
    }

    private void erroMessage(@NotNull MessageReceivedEvent event, String text) {
        event.getChannel().sendMessage(text)
                .queue(msg -> msg.delete().queueAfter(5, TimeUnit.SECONDS));
    }
}

