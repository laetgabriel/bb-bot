package org.laetproject.entities;

public class Experiencia {

    private Integer id;
    private String guild;
    private String user;
    private Double xp;

    public Experiencia(Integer id, String guild, String user, Double xp) {
        this.id = id;
        this.guild = guild;
        this.user = user;
        this.xp = xp;
    }

    public Experiencia() {}

    public String getGuild() {
        return guild;
    }

    public String getUser() {
        return user;
    }

    public Double getXp() {
        return xp;
    }

    public void setXp(Double xp) {
        this.xp = xp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGuild(String guild) {
        this.guild = guild;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
