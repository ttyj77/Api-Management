package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class DailyApiStatisticsDTO {
    private String date;
    private String code;
    private String hh;
    private String name;
    private int totalRequest;
    private int successCnt;
    private int failCnt;
    private String logo;
    private String industry;
}
