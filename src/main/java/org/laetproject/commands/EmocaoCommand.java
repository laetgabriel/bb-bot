package org.laetproject.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;

import java.util.ArrayList;
import java.util.List;

public class EmocaoCommand implements ICommand {
    @Override

    public String getName() {
        return "emocao";
    }

    @Override
    public String getDescription() {
        return "Expresse suas emoções através de um texto";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        OptionData optionTipo = new OptionData(OptionType.STRING, "tipo", "Selecione a emoção", true)
                .addChoice("Um abraço", "abraço")
                .addChoice("Sorrisinho", "sorriso")
                .addChoice("Choro livre", "choro");
        options.add(optionTipo);
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if(command.equals("emocao")){
            OptionMapping option = event.getOption("tipo");
            String tipo = option.getAsString();
            String replyMessage;
            switch(tipo){
                case "abraço" -> replyMessage = "Um abraço, meu querido";
                case "rindo" -> replyMessage = "Que engraçado! Do que está rindo?";
                case "choro" -> replyMessage = "Para de chorar";
                default -> replyMessage = "Emoção desconhecida";
            }
            event.reply(replyMessage).queue();
        }
    }
}
