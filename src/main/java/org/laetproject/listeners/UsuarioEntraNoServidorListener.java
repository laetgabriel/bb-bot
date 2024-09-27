package org.laetproject.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class UsuarioEntraNoServidorListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Member member = event.getMember();
        long roleId = 1222575358738432020L;
        Role role = event.getGuild().getRoleById(roleId);

        if (role != null) {
            event.getGuild().addRoleToMember(member, role).queue();
        }
    }
}
