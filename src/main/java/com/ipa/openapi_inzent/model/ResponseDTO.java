package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ResponseDTO {
    private int id;
    private String respCode;
    private String respMsg;

    private int resParamId;
    private int resId;
    private String key;
    private String type;
    private String sample;
}
