package entities;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class Experiencia {

    private final Guild guild;
    private final User user;
    private Double xp;

    public Experiencia(Guild guild, User user, Double xp) {
        this.guild = guild;
        this.user = user;
        this.xp = xp;
    }

    public Guild getGuild() {
        return guild;
    }

    public User getUser() {
        return user;
    }

    public Double getXp() {
        return xp;
    }

    public void setXp(Double xp) {
        this.xp = xp;
    }
}
