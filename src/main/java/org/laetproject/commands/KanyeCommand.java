package org.laetproject.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.laetproject.Bot;

import java.awt.*;
import java.io.IOException;

public class KanyeCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase(Bot.PREFIX + "kanye")) {
            try {
                String frase = callAPI();
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(Color.BLACK)
                        .setAuthor("YE", null, "https://www.renderhub.com/nammichael/kanye-west-head-low-poly-head-for-game/kanye-west-head-low-poly-head-for-game-01.jpg")
                        .setDescription(frase);

                event.getMessage().replyEmbeds(embed.build()).queue();
            } catch (IOException e) {
                event.getMessage().reply("Cade o YE?").queue();
            }
        }


    }

    private String callAPI() throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.kanye.rest/";

        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            JSONObject json = new JSONObject(response.body().string());
            return json.getString("quote");
        }
    }
}



