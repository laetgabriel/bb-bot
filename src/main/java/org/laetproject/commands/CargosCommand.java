package org.laetproject.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;

import java.util.List;

public class CargosCommand implements ICommand {

    @Override
    public String getName() {
        return "cargos";
    }

    @Override
    public String getDescription() {
        return "Dê uma olhada nos cargo do servidor";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if(command.equals("cargos")){
            event.deferReply().setEphemeral(true).queue();
            StringBuilder cargosStringBuilder = new StringBuilder();
            for (Role role : event.getGuild().getRoles()){

                if (role.isPublicRole()){
                    continue;
                }
                cargosStringBuilder.append(role.getAsMention()).append("\n");
            }

            event.getHook().sendMessage(cargosStringBuilder.toString()).queue();
        }
    }
}
