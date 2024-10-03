package org.laetproject.listeners;

import org.laetproject.Bot;
import org.laetproject.entities.Experiencia;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.laetproject.dao.ExperienciaDAO;
import org.laetproject.dao.impl.ExperienciaDAOImpl;
import org.laetproject.db.DB;

import java.util.List;

public final class ExperienciaEventListener extends ListenerAdapter {

    private final Double XP_PADRAO = 6.5;


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.isFromGuild()) return;

        final ExperienciaDAO experienciaDAO = new ExperienciaDAOImpl(DB.getConnection());
        final List<Experiencia> experiencias = experienciaDAO.listarExperiencia();

        String guildId = event.getGuild().getId();
        String userId = event.getAuthor().getId();
        boolean existeExperiencia = false;

        for (Experiencia exp : experiencias) {
            if (exp.getGuild().equals(guildId) && exp.getUser().equals(userId)) {
                exp.setXp(exp.getXp() + XP_PADRAO);
                experienciaDAO.alterarExperiencia(exp);
                existeExperiencia = true;

                enviarMensagem(event, exp);
                break;
            }
        }
        if (!existeExperiencia) {
            Experiencia experiencia = new Experiencia();
            experiencia.setGuild(guildId);
            experiencia.setUser(userId);
            experiencia.setXp(XP_PADRAO);
            experienciaDAO.inserirExperiencia(experiencia);
        }


    }



    private void enviarMensagem(@NotNull MessageReceivedEvent event, Experiencia exp) {
        double xp = exp.getXp();

        final double TOLERANCIA = 0.001;

        if (Math.abs(xp - 32.5) < TOLERANCIA || Math.abs(xp - 97.5) < TOLERANCIA) {
            String mensagem = String.format(
                    "Parabéns, %s! Você já está com **%.1f de experiência** no servidor! Continue empenhado. 😉",
                    event.getMember().getAsMention(), xp);
            event.getMessage().reply(mensagem).queue();
        }

    }

}
