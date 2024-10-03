package org.laetproject.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.laetproject.dao.CargoDAO;
import org.laetproject.dao.impl.CargoDAOImpl;
import org.laetproject.db.DB;
import org.laetproject.entities.Cargo;

public class UsuarioEntraNoServidorListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        CargoDAO cargoDAO = new CargoDAOImpl(DB.getConnection());
        Member member = event.getMember();
        Cargo cargo = cargoDAO.buscarCargoPorId(event.getGuild().getId());
        if(cargo != null) {
            Role role = event.getGuild().getRoleById(cargo.getRoleId());
            if (role != null) {
                event.getGuild().addRoleToMember(member, role).queue();
            }
        }else
            System.out.println("Cargo de entrada não configurado");
    }
}
