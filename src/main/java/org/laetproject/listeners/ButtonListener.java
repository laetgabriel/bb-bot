package org.laetproject.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;
import org.laetproject.util.BadWordManager;

import java.util.concurrent.TimeUnit;

public class ButtonListener extends ListenerAdapter {

    private final BadWordManager badWordsManager = new BadWordManager();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();

        if (badWordsManager.containsWord(messageContent)) {
            Button buttonRemove = Button.danger("remove:" + event.getMessageId() + ":" + event.getChannel().getId(), "Remover");
            Button buttonIgnore = Button.secondary("ignore", "Ignorar");
            TextChannel staffChannel = event.getGuild().getTextChannelById(1286061506836041780L);
            String jumpLink = event.getJumpUrl();

            if (staffChannel != null && event.getMember() != null && !event.getMember().getUser().isBot()) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Palavra inapropriada detectada")
                            .setAuthor("Por: " + event.getMember().getEffectiveName())
                        .addField("Conteúdo da mensagem:", messageContent, false)
                        .addField("ID do Autor:", event.getMember().getId(), false)
                        .addField("ID da mensagem:", event.getMessageId(), false)
                        .addField("ID do canal:", event.getChannel().getId(), false)
                        .addField("Menção:", event.getMember().getAsMention(), false)
                        .setDescription("[Ir para a mensagem](" + jumpLink + ")");

                MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                        .setEmbeds(embedBuilder.build())
                        .addActionRow(buttonRemove, buttonIgnore);

                staffChannel.sendMessage(messageCreateBuilder.build()).queue();
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String[] buttonData = event.getButton().getId().split(":");

        if (buttonData[0].equals("remove")) {
            event.deferReply().setEphemeral(true).queue();
            String messageId = buttonData[1];
            TextChannel channel = event.getGuild().getTextChannelById(buttonData[2]);

            if (channel != null && messageId != null) {
                channel.retrieveMessageById(messageId).queue(
                        message -> {
                            message.delete().queue(
                                    concluido -> {
                                        event.getHook().sendMessage("Mensagem reportada excluída com sucesso.").setEphemeral(true).queue(
                                                msg -> msg.delete().queueAfter(3L, TimeUnit.SECONDS)
                                        );
                                    },
                                    erro -> {
                                        event.getHook().sendMessage("Erro ao tentar excluir a mensagem.").setEphemeral(true).queue(
                                                msg -> msg.delete().queueAfter(3L, TimeUnit.SECONDS)
                                        );
                                    }
                            );
                        },
                        erro -> {
                            event.getHook().sendMessage("Essa mensagem já foi excluida").setEphemeral(true).queue(
                                    msg -> msg.delete().queueAfter(3L, TimeUnit.SECONDS)
                            );
                        }
                );
            } else {
                event.getHook().sendMessage("Canal ou mensagem inválidos.").setEphemeral(true).queue();
            }
        } else if (event.getButton().getId().equals("ignore")) {
            event.deferReply().setEphemeral(true).queue();

            event.getChannel().retrieveMessageById(event.getMessageId()).queue(
                    message -> {
                        message.delete().queue(
                                concluido -> {
                                    event.getHook().sendMessage("Alerta removido").setEphemeral(true).queue(
                                            msg -> msg.delete().queueAfter(3L, TimeUnit.SECONDS)
                                    );
                                },
                                erro -> {
                                    event.getHook().sendMessage("Ocorreu um erro ao excluir o alerta").setEphemeral(true).queue(
                                            msg -> msg.delete().queueAfter(3L, TimeUnit.SECONDS)
                                    );
                                }
                        );
                    },
                    erro -> {
                        event.getHook().sendMessage("Ocorreu um erro inesperado. Tente novamente.").setEphemeral(true).queue(
                                msg -> msg.delete().queueAfter(3L, TimeUnit.SECONDS)
                        );
                    }
            );
        }
    }
}