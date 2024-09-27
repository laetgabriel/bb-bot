package org.laetproject.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.internal.interactions.component.StringSelectMenuImpl;
import org.jetbrains.annotations.NotNull;
import org.laetproject.Bot;

import java.awt.*;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] command = event.getMessage().getContentRaw().trim().split(" ");
        if (command[0].equalsIgnoreCase(Bot.PREFIX + "help")) {

            EmbedBuilder embed = getEmbedBuilder();
            StringSelectMenu commands = getSelectMenu();

            MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder()
                    .addContent(event.getMember().getAsMention())
                    .addEmbeds(embed.build())
                    .addActionRow(commands.asEnabled());

            event.getChannel().sendMessage(messageCreateBuilder.build()).queue();
        }
    }


    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();

        switch (event.getInteraction().getValues().get(0)) {
            case "sortear" ->
                    event.getHook().sendMessage(createEmbedOption("Ajuda para: sortear", "Se for usar um arquivo, atenda aos critérios de:\n" +
                            "- Título dos filmes em inglês\n" +
                            "- Separados por quebra de linha (um filme por linha)\n\n" +
                            "Se for usar uma lista de filmes por escrito, atenda aos critérios:\n" +
                            "- Título dos filmes em inglês\n" +
                            "- Filmes entre aspas e separados por virgula, por exemplo: \"Filme1\", \"Filme2\", \"Filme3\"\n" +
                            "**É possível utilizar as duas formas de inserção de filmes ao mesmo tempo**", "Uso", """
                            `/sortear <filmes>
                            /sortear <arquivo.txt>
                            /sortear <filmes> <arquivo.txt>`""")
                            .build()).setEphemeral(true).queue();

            case "recado" ->
                    event.getHook().sendMessage(createEmbedOption("Ajuda para: recado", "O BBBot envia um recado 😯", "Uso", "`/recado <recado>`")

                            .build()).setEphemeral(true).queue();
            case "emocao" ->
                    event.getHook().sendMessage(createEmbedOption("Ajuda para: emocao", "Selecione as emoções pré-definidas para receber uma mensagem", "Uso", "`/emocao`")
                            .build()).setEphemeral(true).queue();

            case "info" ->
                    event.getHook().sendMessage(createEmbedOption("Ajuda para: info", "Informações básicas sobre você.", "Uso", "`/info`")
                            .build()).setEphemeral(true).queue();

            case "darcargo" ->
                    event.getHook().sendMessage(createEmbedOption("Ajuda para: darcargo", "O BBBOt atribui um cargo que esteja abaixo dele a algum membro", "Uso", "`/darcargo <membro> <cargo>`")
                            .build()).setEphemeral(true).queue();

            case "cargo" ->
                    event.getHook().sendMessage(createEmbedOption("Ajuda para: cargo", "Lista os cargos do servidor", "Uso", "`/cargo`")
                            .build()).setEphemeral(true).queue();

        }
    }

    @NotNull
    private EmbedBuilder getEmbedBuilder() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0x0BFF00));
        embed.setTitle("Help");

        embed.setDescription("""
                --------------------------
                        **Help BBBot**
                ---------------------------""");

        embed.addField("**Comandos de barra**",
                "`sortear`: _Insira uma lista de filmes para sortear_" +
                        "\n`recado`: _Manda um recado por BBBot_ 😯" +
                        "\n`emocao`: _Manda uma mensagem com emoções pré-definidas_" +
                        "\n`info`: _Manda informações básicas sobre você_" +
                        "\n`darcargo`: _O BBBot adiciona um cargo a algum membro_" +
                        "\n`cargo`: _Lista os cargos do servidor_" +
                        "\n`addpalavra`: **Depreciado**", false);
        return embed;
    }

    private MessageCreateBuilder createEmbedOption(String title, String description, String field, String valueField) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0x000000));
        embed.setTitle(title);
        embed.setDescription(description);
        embed.addField(field, "`" + valueField + "`", false);
        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder.setEmbeds(embed.build());

        return messageCreateBuilder;
    }

    @NotNull
    private StringSelectMenu getSelectMenu() {
        return StringSelectMenu.create("menu:help")
                .setPlaceholder("Escolha um comando para mais informações")
                .setRequiredRange(1, 1)
                .addOption("sortear", "sortear", "Insira uma lista de filmes para sortear")
                .addOption("recado", "recado", "Manda um recado por BBBot ")
                .addOption("emocao", "emocao", "Manda uma mensagem com emoções pré-definidas")
                .addOption("info", "info", "Manda informações básicas sobre você")
                .addOption("darcargo", "darcargo", "O BBBot adiciona um cargo a algum membro")
                .addOption("cargo", "cargo", "Lista os cargos do servidor")
                .build();
    }
}