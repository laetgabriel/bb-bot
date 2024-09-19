package org.laetproject.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.laetproject.commands.config.ICommand;
import org.laetproject.commands.exceptions.MemberNotFoundException;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InfoCommand implements ICommand {


    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "Info sobre o membro";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Member member = event.getMember();
        if(command.equals("info")){
            if(member == null) {
                event.reply("Membro não encontrado").setEphemeral(true).queue();
                throw new MemberNotFoundException("Membro não encontrado");
            }
            event.deferReply().setEphemeral(true).queue();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(new Color(0x0AB5EC));
            embed.setTitle("Informações do membro");
            embed.setDescription("Suas informações (só você consegue ver isso)");
            embed.setThumbnail(member.getUser().getAvatarUrl());
            embed.addField("Membro", member.getAsMention(), false);
            embed.addField("Criação da conta: ", member.getTimeCreated().toLocalDateTime().minusHours(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), false);
            embed.addField("Entrou no servidor: ", member.getTimeJoined().toLocalDateTime().minusHours(3).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), false);

            String cargos = member.getRoles().isEmpty() ? "Nenhum cargo atribuído a você." : member.getRoles().stream().map(Role::getName).collect(Collectors.joining(", "));
            embed.addField("Cargo", cargos, false);
            if(member.isBoosting()){
                embed.addField("Boostando o servidor desde: " , member.getTimeBoosted().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), false);
            }

            embed.setFooter("Informações fornecidas por " + event.getJDA().getSelfUser().getName() + " | " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            event.getHook().sendMessageEmbeds(embed.build()).queue();
        }
    }
}