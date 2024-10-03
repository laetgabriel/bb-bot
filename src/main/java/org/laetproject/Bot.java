package org.laetproject;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.laetproject.commands.*;
import org.laetproject.commands.config.CommandManager;
import org.laetproject.listeners.ButtonBadWordListener;
import org.laetproject.listeners.ExperienciaEventListener;
import org.laetproject.listeners.UsuarioEntraNoServidorListener;

import javax.security.auth.login.LoginException;

/*
 * Classe destinada a configurar o Bot
 *
 * @author Gabriel Laet
 */

public final class Bot {

    private final ShardManager shardManager;
    private final CommandManager commandManager = new CommandManager();
    public final static String PREFIX = "bb!";

    /*
     * Carrega as configurações do bot e pré-define suas atividades
     *
     * @throws LoginException ocorre quando o token é inválido/null
     */

    public Bot() throws LoginException {
        Dotenv config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.listening("bb!help"))
                .enableIntents(GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES);
                //.setMemberCachePolicy(MemberCachePolicy.ALL)
                //.setChunkingFilter(ChunkingFilter.ALL)
                //.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY)
        shardManager = builder.build();
        registerListeners();
    }

    private void registerListeners() {
        registerCommands();
        shardManager.addEventListener(new SetarCargoCommand(), new StatusExperienciaCommand(), new StatusServerCommand(), new SkinCommand(), new ExperienciaEventListener(), new HelpCommand(), new UsuarioEntraNoServidorListener(), commandManager, new ButtonBadWordListener());
    }

    private void registerCommands() {
        commandManager.add(new RecadoCommand());
        commandManager.add(new EmocaoCommand());
        commandManager.add(new CargosCommand());
        commandManager.add(new DarCargoCommand());
        commandManager.add(new InfoCommand());
        commandManager.add(new AdicionarPalavraCommand());
        commandManager.add(new SorteioFilmeCommand());
    }

}

