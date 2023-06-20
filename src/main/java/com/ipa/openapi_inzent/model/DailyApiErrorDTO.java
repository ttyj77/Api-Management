package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class DailyApiErrorDTO {
    private String resCode;
    private int count;
}
