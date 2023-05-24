package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ResParamDTO {
    private int id;
    private int resId;
    private String key;
    private String value;
    private String type;
    private String sample;
}
