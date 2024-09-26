package org.laetproject.commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.laetproject.commands.config.ICommand;
import org.json.*;

import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class SorteioFilmeCommand implements ICommand {

    private final Dotenv dotenv = Dotenv.load();

    @Override
    public String getName() {
        return "sortear";
    }

    @Override
    public String getDescription() {
        return "Use para sortear os filmes";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "filme", "Nome do filme que você deseja incluir no sorteio (separe por vírgulas)", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("sortear")) {
            event.deferReply().setEphemeral(false).queue();

            OptionMapping option = event.getOption("filme");
            String[] filmes = option.getAsString().split("\"(,\\s*)?\"");
            int filmeSorteado = new Random().nextInt(filmes.length);
            String filme = filmes[filmeSorteado];

            String apiKey = dotenv.get("API_KEY");
            OkHttpClient client = new OkHttpClient();

            String encodedFilme = URLEncoder.encode(filme, StandardCharsets.UTF_8);
            String url = "https://www.omdbapi.com/?t=" + encodedFilme + "&apikey=" + apiKey;

            Request request = new Request.Builder().url(url).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Erro ao fazer requisição");
                }
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);

                if (json.has("Title")) {
                    EmbedBuilder embedMovie = new EmbedBuilder();
                    embedMovie.setColor(new Color(50, 205, 50));
                    embedMovie.setTitle("🎬 **Sorteio dos Irmões** 🎲");
                    embedMovie.setDescription("**A hora do sorteio chegou!**\n O " +
                            "filme vencedor será exibido em breve." + " Preparados?");
                    embedMovie.addField("🍿 **Filme Sorteado**", json.getString("Title") + " - (" + json.getString("Year") + ")", true);
                    embedMovie.addField("🦘**Gênero**", json.getString("Genre"), true);
                    embedMovie.addField("📊**Avaliação**", json.getString("imdbRating") + "/10", true);
                    embedMovie.addField("✍\uFE0F **Sinopse**", json.getString("Plot"), false);
                    embedMovie.addField("🎥 **Total de Filmes no Sorteio**", String.valueOf(filmes.length), true);
                    embedMovie.addField("🎉 **Parabéns ao Filme Vencedor!**", "Prepare a pipoca 🍿 e aproveite o show!", true);
                    embedMovie.setThumbnail(json.getString("Poster"));
                    embedMovie.setFooter(
                            "Sorteio realizado por " + event.getUser().getName() + " | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                            event.getUser().getAvatarUrl()
                    );

                    MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                            .setEmbeds(embedMovie.build());
                    event.getHook().sendMessage(messageCreateBuilder.build()).queue();
                } else {
                    event.getHook().sendMessage("Não consegui encontrar o filme sorteado 😿").queue();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

