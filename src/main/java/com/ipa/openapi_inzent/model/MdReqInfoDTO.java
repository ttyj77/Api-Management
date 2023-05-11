package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class MdReqInfoDTO {
    private int id;
    private int providerId;
    private String code;
    private String agencyName;
    private String serviceName;
    private String consumerNum;
    private String reqSEQ;
    private String reqType;
    private String tokenExpiryDate;

}
