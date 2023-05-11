package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class MdProviderDTO {
    private int id;
    private String reqDate;

    private String reqTime;

    // 입력받을 때 x100 해서 정수로 만들꺼임
    private int runtime;

    // resDate 는 reqDate + runtime
    private String resDate;

    private String resCode;
    private String apiCode;
    private String customerNum;
    private String regularTransmission;
    private String uniqueNum;
    private String statusInfo;
    private String apiResources;
    private String reqHeader;
    private String resMsg;
    private String resData;

    private MdReqInfoDTO mdReqInfoDTO;

}
