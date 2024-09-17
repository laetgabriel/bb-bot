package org.laetproject;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

import javax.security.auth.login.LoginException;

public class Bot {

    public Bot() throws LoginException {
        String token = System.getenv("TOKEN");
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setActivity(Activity.listening("Mamonas Assassinas")).build();

    }

}

