package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class UserRoleDTO {
    private int userId;
    private int roleId;

    // role
    private int id;
    private String code;
    private String name;
}
