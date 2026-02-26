package com.genoutbound.gateway.web.admin.dto;

public class PermissionListItem {

    private Long id;
    private String code;
    private String description;

    public PermissionListItem(Long id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
