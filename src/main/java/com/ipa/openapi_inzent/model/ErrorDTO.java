package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ErrorDTO {
    private int id;
    private String error;
    private String reason;
    private int seq;
}
