package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class MdCollectorDTO {
    private int id;
    private String reqDate;

    private String reqTime;

    // 입력받을 때 x100 해서 정수로 만들꺼임
    // 그냥 바로 double 함
//    private double runtime;
    private int runtime;

    // resDate 는 reqDate + runtime
    private String resDate;

    private String resCode;
    private String apiCode;
    private String regularTransmission;
    private String targetHost;
    private String uniqueNum;
    private String apiResources;
    private String reqHeader;
    private String resMsg;
    private String resData;

}
