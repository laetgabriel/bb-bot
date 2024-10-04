package org.laetproject.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.laetproject.Bot;
import org.laetproject.dao.CargoDAO;
import org.laetproject.dao.impl.CargoDAOImpl;
import org.laetproject.db.DB;
import org.laetproject.entities.Cargo;

import java.util.List;

public class SetarCargoCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (args[0].equalsIgnoreCase(Bot.PREFIX + "setcargo")) {
            if (event.getAuthor().isBot()) return;

            if (!event.isFromGuild()) return;

            if(event.getMember() == null) return;

            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.getMessage().reply("Você não tem permissão para executar isso").queue();
                return;
            }

            List<Role> rolesList = event.getMessage().getMentions().getRoles();
            if (rolesList.size() != 1) {
                event.getMessage().reply("Insira o cargo corretamente `bb!setcargo <@cargo>`").queue();
                return;
            }

            Role role = rolesList.get(0);

            try {
                CargoDAO cargoDAO = new CargoDAOImpl(DB.getConnection());
                Cargo cargo = cargoDAO.buscarCargoPorId(event.getGuild().getId());
                if (cargo == null) {
                    cargoDAO.inserirCargo(new Cargo(null, event.getGuild().getId(), role.getIdLong()));
                    event.getMessage().reply("Cargo padrão join setado").queue();
                } else {
                    cargo.setRoleId(role.getIdLong());
                    cargoDAO.alterarCargo(cargo);
                    event.getMessage().reply("Cargo de entrada atualizado").queue();
                }
            } catch (Exception e) {
                event.getMessage().reply("Ocorreu um erro inesperado. Como fez isso? 🤔").queue();
            }
        }
    }
}
