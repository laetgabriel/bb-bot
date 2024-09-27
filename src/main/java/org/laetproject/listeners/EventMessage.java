package org.laetproject.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public final class EventMessage extends ListenerAdapter {

    /*
    Evento sem utilidade.
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();

        if (user == null || user.isBot())
            return;

        String emoji = event.getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        String message = user.getAsTag() + " reagiu a mensagem com " + emoji + " no canal: " + channelMention + ". Link para mensagem: " + jumpLink;
        event.getChannel().sendMessage(message).queue();
    }

     */

    /*
    Evento sem utilidade.
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        User user = event.getUser();
        System.out.println(user.getEffectiveAvatarUrl());
    }

     */

    /*
    Evento sem utilidade. Consome muita memória cache
    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        List<Member> members = event.getGuild().getMembers();
        int cont = 0;
        for (Member member : members) {
            if(member.getOnlineStatus().equals(OnlineStatus.ONLINE)){
                cont++;
            }
        }
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage("Agora são " + cont + " membros online!").queue();
    }
     */
}
