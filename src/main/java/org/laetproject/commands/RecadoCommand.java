package org.laetproject.commands;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.laetproject.commands.config.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RecadoCommand implements ICommand {

    @Override
    public String getName() {
        return "recado";
    }

    @Override
    public String getDescription() {
        return "Faz o bot dizer algo";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        OptionData optionRecado = new OptionData(OptionType.STRING, "recado", "Mensagem que você quer que o bot diga", true);
        OptionData optionChannel = new OptionData(OptionType.CHANNEL, "canal", "Canal que você deseja enviar essa mensagem")
                .setChannelTypes(ChannelType.NEWS, ChannelType.TEXT, ChannelType.GUILD_PUBLIC_THREAD);
        options.add(optionRecado);
        options.add(optionChannel);
        return options;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("recado")) {
            OptionMapping option1 = event.getOption("recado");
            OptionMapping option2 = event.getOption("canal");
            MessageChannel channel;

            if (option1 == null) {
                event.reply("O recado não pode ser nulo.").setEphemeral(true).queue();
                return;
            }

            if (option2 != null && option2.getAsChannel().getType().isMessage()) {
                channel = option2.getAsChannel().asTextChannel();
            } else {
                channel = event.getChannel();
            }

            channel.sendMessage(option1.getAsString()).queue();
            event.reply("Seu recado foi enviado.").setEphemeral(true).queue(msg ->
                    msg.deleteOriginal().queueAfter(2, TimeUnit.SECONDS));
        }
    }
}
