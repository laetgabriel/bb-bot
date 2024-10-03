package org.laetproject.entities;

public class Cargo {

    private Integer id;
    private String guildId;
    private Long roleId;

    public Cargo(Integer id, String guildId, Long roleId) {
        this.id = id;
        this.guildId = guildId;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGuildId() {
        return guildId;
    }

    public void setGuildId(String guildId) {
        this.guildId = guildId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
