package org.laetproject.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {

    private final List<CommandData> commands = new ArrayList<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();

        if(command.equals("ajuda")){
            String mention = event.getUser().getAsMention();

            event.reply("Olá, " + mention + "! Sou um bot limitado e em **desenvolvimento.**").setEphemeral(true).queue(msg ->{
                msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);

            });

        }
        else if (command.equals("cargos")) {
            event.deferReply().setEphemeral(true).queue();
            List<Role> roles = event.getGuild().getRoles();

            if (roles.isEmpty()) {
                event.getHook().sendMessage("Nenhum cargo encontrado no servidor.").queue();
                return;
            }

            StringBuilder roleStringBuilder = new StringBuilder();

            for (Role role : roles) {

                if (role.isPublicRole())
                    continue;

                roleStringBuilder.append(role.getAsMention()).append("\n");
            }

            event.getHook().sendMessage(roleStringBuilder.toString()).queue();

        }
        else if (command.equals("recado")) {
            OptionMapping option1 = event.getOption("recado");
            OptionMapping option2 = event.getOption("canal");
            MessageChannel channel;
            if(option2 != null){
                channel = option2.getAsChannel().asTextChannel();
            }else
                channel = event.getChannel();
            channel.sendMessage(option1.getAsString()).queue();
            event.reply("Seu recado foi enviado.").setEphemeral(true).queue(msg -> msg.deleteOriginal().queueAfter((long) 2.5, TimeUnit.SECONDS));
        }
        else if (command.equals("emocao")) {
            OptionMapping option = event.getOption("tipo");
            String tipo = option.getAsString();

            String replyMessage = "";

            switch (tipo.toLowerCase()){
                case "abraço" -> replyMessage = "Um abraço, meu querido";

                case "rindo" -> replyMessage = "Que engraçado! Do que está rindo?";

                case "choro" -> replyMessage = "Para de chorar";
            }
            event.reply(replyMessage).queue();
        }
        else if (command.equals("darcargo")) {
            OptionMapping option = event.getOption("membro");
            OptionMapping option2 = event.getOption("cargo");

            Member member = option.getAsMember();
            Role role = option2.getAsRole();

            event.getGuild().addRoleToMember(member, role).queue();
            event.reply("Cargo" + role.getAsMention() + " adicionado ao membro " + member.getAsMention() +" com sucesso").setEphemeral(true).queue();
        }
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        registerCommands();
        event.getGuild().updateCommands().addCommands(commands).queue();
    }

    private void registerCommands(){
        commands.clear();
        commands.add(Commands.slash("ajuda", "Precisa de ajuda com o bot? Use"));
        commands.add(Commands.slash("cargos", "Verifique os cargos do servidor"));

        OptionData optionRecado = new OptionData(OptionType.STRING, "recado", "Mensagem que você quer que o bot diga", true);
        OptionData optionChannel = new OptionData(OptionType.CHANNEL, "canal", "Canal que você deseja enviar essa mensagem")
                .setChannelTypes(ChannelType.NEWS, ChannelType.TEXT, ChannelType.GUILD_PUBLIC_THREAD);
        commands.add(Commands.slash("recado", "Faz o bot dizer algo").addOptions(optionRecado, optionChannel));

        OptionData optionTipo = new OptionData(OptionType.STRING, "tipo", "Tipo de emoção", true)
                .addChoice("Um abraço", "abraço")
                .addChoice("Sorrisos", "rindo")
                .addChoice("Choradeira", "choro");
        commands.add(Commands.slash("emocao", "Expresse suas emoções através de um texto ").addOptions(optionTipo));

        OptionData optionUser = new OptionData(OptionType.USER, "membro", "Membro que seja atribuir o cargo", true);
        OptionData optionRole = new OptionData(OptionType.ROLE, "cargo", "Cargo que deseja atribuir ao membro", true);
        commands.add(Commands.slash("darcargo", "Dê cargos para membros do seu servidor").addOptions(optionUser, optionRole));
    }
}
