package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ApiDTO {
    private int id;
    private String context;
    private String name;
    private boolean disclosure;
    private String explanation;

    // apisRole
    private int apisId;
    private int roleId;

    // role
    private String code;
}
