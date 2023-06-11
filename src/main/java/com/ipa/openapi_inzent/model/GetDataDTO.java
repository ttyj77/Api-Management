package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class GetDataDTO {
    private int id;
    private String requestData;
    private String responseData;
    private String uri;
    private String apiId;
    private String clientNum;
    private String industry;
}
