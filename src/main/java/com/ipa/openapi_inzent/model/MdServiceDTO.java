package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MdServiceDTO {
    private int id;
    private int agencyId;
    private String clientId;
    private String mdServiceName;
    private String callbackUrl;

    private String domainName;

}
