package org.laetproject;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.laetproject.commands.AjudaCommand;
import org.laetproject.commands.DarCargoCommand;
import org.laetproject.commands.EmocaoCommand;
import org.laetproject.commands.CargosCommand;
import org.laetproject.commands.config.CommandManager;
import org.laetproject.commands.RecadoCommand;
import org.laetproject.listeners.EventMessage;

import javax.security.auth.login.LoginException;

/*
 * Classe destinada a configurar o Bot
 *
 * @author Gabriel Laet
 */

public class Bot {

    private final Dotenv config;
    private final ShardManager shardManager;
    private final CommandManager commandManager = new CommandManager();

    /*
     * Carrega as configurações do bot e pré-define suas atividades
     *
     * @throws LoginException ocorre quando o token é inválido/null
     */

    public Bot() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.listening("Mamonas Assassinas"))
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
        shardManager.addEventListener(new EventMessage(), commandManager);
    }

    private void registerCommands() {
        commandManager.add(new AjudaCommand());
        commandManager.add(new RecadoCommand());
        commandManager.add(new EmocaoCommand());
        commandManager.add(new CargosCommand());
        commandManager.add(new DarCargoCommand());
    }

}

