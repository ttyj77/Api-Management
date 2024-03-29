package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class MdReqInfoDTO {
    private int id;
    private String code;
    private String clientNum;
    private String agencyName;
    private String serviceName;
    private String reqType;
    private String reqSEQ;
    private Date tokenExpiryDate;


}
