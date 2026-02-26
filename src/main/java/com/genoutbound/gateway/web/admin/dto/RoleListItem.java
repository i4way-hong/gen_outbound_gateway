package com.genoutbound.gateway.web.admin.dto;

public class RoleListItem {

    private Long id;
    private String name;
    private boolean enabled;
    private int permissionCount;

    public RoleListItem(Long id, String name, boolean enabled, int permissionCount) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.permissionCount = permissionCount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getPermissionCount() {
        return permissionCount;
    }
}
