package org.laetproject;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.security.auth.login.LoginException;

public class Bot {

    private final Dotenv config;

    public Bot() throws LoginException {
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.listening("Mamonas Assassinas")).build();

    }

    public Dotenv getConfig() {
        return config;
    }

}

