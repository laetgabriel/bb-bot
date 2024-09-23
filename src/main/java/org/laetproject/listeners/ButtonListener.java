package org.laetproject.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {

    private final String[] BAD_WORDS = {"fdp", "lixo", "pnc", "seu merda", "pau no cu", "pau", "cu"};

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
        for (String badWord : BAD_WORDS) {
            if (event.getMessage().getContentRaw().contains(badWord)) {

                Button buttonRemove = Button.danger("remove", "Remover");
                TextChannel staffChannel = event.getGuild().getTextChannelById(1286061506836041780L);
                String jumpLink = event.getJumpUrl();

                if (staffChannel != null && event.getMember() != null) {
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setTitle("Palavra inapropriada detectada")
                            .setAuthor("Por: " + event.getMember().getEffectiveName())
                            .addField("Conteúdo da mensagem:", messageContent, false)
                            .addField("ID do Autor:", event.getMember().getId(), false)
                            .addField("Menção:", event.getMember().getAsMention(), false)
                            .setDescription("[Ir para a mensagem](" + jumpLink + ")");

                    MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                            .setEmbeds(embedBuilder.build())
                            .addActionRow(buttonRemove);

                    staffChannel.sendMessage(messageCreateBuilder.build()).queue();
                }
            }
        }
    }
}
