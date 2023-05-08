package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiDTO {
    private int id;
    private String context;
    private String name;
    private boolean disclosure;
    private String explanation;
}
