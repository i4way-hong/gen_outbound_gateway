package com.genoutbound.gateway.web.admin.dto;

public class UserListItem {

    private Long id;
    private String username;
    private boolean enabled;
    private String roles;

    public UserListItem(Long id, String username, boolean enabled, String roles) {
        this.id = id;
        this.username = username;
        this.enabled = enabled;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getRoles() {
        return roles;
    }
}
