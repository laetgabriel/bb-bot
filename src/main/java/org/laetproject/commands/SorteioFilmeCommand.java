package org.laetproject.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;
import org.laetproject.util.MovieManager;

import java.util.List;

public class SorteioFilmeCommand implements ICommand {

    private MovieManager movieManager = new MovieManager();

    @Override
    public String getName() {
        return "addfilme";
    }

    @Override
    public String getDescription() {
        return "Use para sorteiar os filmes adicionados pelos membros";
    }

    @Override
    public List<OptionData> getOptions() {

        return List.of(new OptionData(OptionType.STRING, "filme", "Nome do filme que você deseja incluir no sorteio", true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if(command.equals("addfilme")) {
            event.deferReply().setEphemeral(true).queue();
            OptionMapping option = event.getOption("filme");
            movieManager.writeLine(option.getAsString(), "filmes.txt");
            event.getHook().sendMessage("Filme adicionado").setEphemeral(true).queue();
        }
    }
}
