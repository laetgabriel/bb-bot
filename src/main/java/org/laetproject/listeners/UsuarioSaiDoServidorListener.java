package org.laetproject.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.laetproject.dao.impl.ExperienciaDAOImpl;
import org.laetproject.db.DB;
import org.laetproject.entities.Experiencia;

public class UsuarioSaiDoServidorListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        ExperienciaDAOImpl experienciaDAO = new ExperienciaDAOImpl(DB.getConnection());

        Experiencia experiencia = experienciaDAO.buscarCargoPorIdGuildEIdUser(event.getGuild().getId(), event.getUser().getId());
        if (experiencia != null) {
            experienciaDAO.excluirExperiencia(experiencia);
        }
    }
}
