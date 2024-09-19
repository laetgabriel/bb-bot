package org.laetproject.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AjudaCommand implements ICommand {


    @Override
    public String getName() {
        return "ajuda";
    }

    @Override
    public String getDescription() {
        return "Precisa de ajuda em relação ao bot? Use";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        User user = event.getUser();
        if(command.equals("ajuda")){
            event.reply("Olá, " + user.getAsMention() + ". Bot limitado e em desenvolvimento").queue(msg -> {
                msg.deleteOriginal().queueAfter(5L, TimeUnit.SECONDS);
            });
        }
    }

}
