package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ParameterDTO {
    private int id;
    private int apiDetailsId;
    private String name;
    private String transferMethod;
    private String explanation;
    private String type;
    private Boolean required;
    private String sample;

}
