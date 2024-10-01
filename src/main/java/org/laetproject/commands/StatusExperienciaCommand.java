package org.laetproject.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.laetproject.Bot;
import org.laetproject.dao.ExperienciaDAO;
import org.laetproject.dao.impl.ExperienciaDAOImpl;
import org.laetproject.db.DB;
import org.laetproject.entities.Experiencia;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StatusExperienciaCommand extends ListenerAdapter {

    private final ExperienciaDAO experienciaDAO = new ExperienciaDAOImpl(DB.getConnection());

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        List<Experiencia> experiencias = experienciaDAO.listarExperiencia();

        String[] command = event.getMessage().getContentRaw().split(" ");

        if (command[0].equalsIgnoreCase(Bot.PREFIX + "statusxp")) {
            for (Experiencia experiencia : experiencias) {
                if (experiencia.getGuild().equals(event.getGuild().getId()) && experiencia.getUser().equals(event.getAuthor().getId())) {
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setColor(new Color(0x3498db))
                            .setTitle("📊 Status de Experiência - **" + event.getGuild().getName() + "**")
                            .setThumbnail(event.getGuild().getIconUrl())
                            .setFooter("⚔️ Confira o Rank do Servidor com " + Bot.PREFIX + "bb!top", event.getAuthor().getAvatarUrl())
                            .addField("👤 Usuário", event.getAuthor().getAsTag(), true)
                            .addField("🔢 Nível de Experiência", experiencia.getXp() + " XP", true)
                            .addField("🏅 Ranking", getRankingAtual(experiencia, experiencias, event.getGuild().getId()) + "º no Servidor", false) // Ranking no servidor
                            .setDescription("🔎 **Status atualizado automaticamente!**");

                    event.getMessage().replyEmbeds(embedBuilder.build()).queue();
                    break;
                }
            }
        } else if (command[0].equalsIgnoreCase(Bot.PREFIX + "top")) {
            List<Experiencia> topExperiencias = getTopExperiencia(experiencias, event.getGuild().getId());

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setColor(new Color(0xFFD700))
                    .setTitle("Top 10 Membros - " + event.getGuild().getName())
                    .setThumbnail(event.getGuild().getIconUrl())
                    .setFooter("Intereja Para Entrar no Ranking!");

            StringBuilder ranking = new StringBuilder();

            for (int i = 0; i < Math.min(topExperiencias.size(), 10); i++) {
                Experiencia experiencia = topExperiencias.get(i);

                String medalha = switch (i) {
                    case 0 -> "🥇";
                    case 1 -> "🥈";
                    case 2 -> "🥉";
                    default -> (i + 1) + "º";
                };

                ranking.append(medalha)
                        .append("<@")
                        .append(experiencia.getUser())
                        .append("> • ")
                        .append(experiencia.getXp())
                        .append("**XP**\n");
            }
            embedBuilder.addField("\uD83C\uDFC5 **Ranking Atual**", ranking.toString(), false);
            event.getMessage().replyEmbeds(embedBuilder.build()).queue();
        }
    }

    private Integer getRankingAtual(Experiencia experiencia, List<Experiencia> experiencias, String guildId) {
        List<Experiencia> experienciasOrdenadas = getTopExperiencia(experiencias, guildId);

        for (int i = 0; i < experienciasOrdenadas.size(); i++) {
            if (experienciasOrdenadas.get(i).getUser().equals(experiencia.getUser()) &&
                    experienciasOrdenadas.get(i).getGuild().equals(experiencia.getGuild())) {
                return i + 1;
            }
        }
        return null;
    }

    private List<Experiencia> getTopExperiencia(List<Experiencia> experiencias, String guildId) {
        List<Experiencia> experienciasTop = new ArrayList<>();
        for (Experiencia experiencia : experiencias) {
            if (experiencia.getGuild().equals(guildId)) {
                experienciasTop.add(experiencia);
            }
        }
        experienciasTop.sort((a, b) -> Double.compare(b.getXp(), a.getXp()));

        return experienciasTop;
    }
}