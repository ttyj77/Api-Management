package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class MdAgencyDTO {

    private int id;
    private String code;
    private String division;
    private String name;
    private String industry;
    private String address;
    private String domainName;
    private String publicApiIp;
    private String authenticationMethod;
    private String TLSNum;
    private String agencyIp;
    private String agencyPort;

    // +
    private MdServiceDTO mdServiceDTO;
    private MdTokenDTO mdTokenDTO;

}
