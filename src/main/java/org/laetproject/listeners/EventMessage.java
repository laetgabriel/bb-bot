package org.laetproject.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class EventMessage extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();

        if (user == null || user.isBot())
            return;

        String emoji = event.getEmoji().getAsReactionCode();
        String channelMention = event.getChannel().getAsMention();
        String jumpLink = event.getJumpUrl();

        String message = user.getAsTag() + " reagiu a mensagem com " + emoji + " no canal: " + channelMention + ".";
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String messageContent = message.getContentRaw();
        User user = event.getAuthor();

        if (user.isBot())
            return;

        if (messageContent.contains("fdp") || messageContent.contains("lixo")) {
            message.delete().queue();
            event.getChannel().sendMessage("Cuidado com o que fala!").queue(msg ->  {
                msg.delete().queueAfter(3, TimeUnit.SECONDS);
            });
        }
    }
}
