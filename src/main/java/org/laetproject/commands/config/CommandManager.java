package org.laetproject.commands.config;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CommandManager extends ListenerAdapter {

    private final List<ICommand> commands = new ArrayList<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for (ICommand command : commands) {
            if (command.getName().equalsIgnoreCase(event.getName())) {
                command.execute(event);
                return;
            }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (ICommand command : commands) {
                guild.upsertCommand(command.getName(), command.getDescription())
                        .addOptions(command.getOptions())
                        .queue(
                                success -> System.out.println("Comando '" + command.getName() + "' registrado no servidor: " + guild.getName()),
                                failure -> System.err.println("Falha ao registrar o comando '" + command.getName() + "' no servidor: " + guild.getName())
                        );
            }
        }
    }

    public void add(ICommand command) {
        commands.add(command);
    }

}
