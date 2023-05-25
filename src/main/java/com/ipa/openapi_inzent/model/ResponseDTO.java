package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ResponseDTO {
    private int id;
    private int apiDetailsId;
    private String respCode;
    private String respMsg;

    private String type;


}
