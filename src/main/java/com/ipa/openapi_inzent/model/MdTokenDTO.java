package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class MdTokenDTO {
    private int tokenId;
    private int mdServiceId;
    private String consumerNum;
    private Date createDate;
    private Date endDate;
    private String accessToken;

}
