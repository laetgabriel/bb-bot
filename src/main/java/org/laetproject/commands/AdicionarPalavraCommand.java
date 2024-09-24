package org.laetproject.commands;


import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;
import org.laetproject.util.BadWordsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AdicionarPalavraCommand implements ICommand {
    private BadWordsManager badWordsManager = new BadWordsManager();

    @Override
    public String getName() {
        return "addpalavra";
    }

    @Override
    public String getDescription() {
        return "Adiciona um palavrão ao arquivo de configuração";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        OptionData option = new OptionData(OptionType.STRING, "palavra", "Palavra de baixo cunho que deseja adicionar", true);
        options.add(option);
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if(command.equals("addpalavra")){
            event.deferReply().setEphemeral(true).queue();
            OptionMapping option = event.getOption("palavra");
            badWordsManager.writeLine(option.getAsString());
            event.getHook().sendMessage("Palavra adicionada").queue();
        }
    }
}
