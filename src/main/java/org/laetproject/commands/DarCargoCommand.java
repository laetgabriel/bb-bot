package org.laetproject.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DarCargoCommand implements ICommand {

    @Override
    public String getName() {
        return "darcargo";
    }

    @Override
    public String getDescription() {
        return "Adiciona um cargo especifico em algum membro";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        OptionData optionUser = new OptionData(OptionType.USER, "membro", "Escolha o membro que deseja atribuir o cargo", true);
        OptionData optionRole = new OptionData(OptionType.ROLE, "cargo", "Escolha o cargo que deseja atribuir ao membro", true);
        options.add(optionUser);
        options.add(optionRole);
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if(command.equals("darcargo")) {

            event.deferReply().setEphemeral(true).queue();
            OptionMapping optionMember = event.getOption("membro");
            OptionMapping optionRole = event.getOption("cargo");

            event.getGuild().addRoleToMember(optionMember.getAsMember(), optionRole.getAsRole()).queue();
            event.getHook().sendMessage("Cargo atribuido com sucesso").queue();
        }
    }
}
