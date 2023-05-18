package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class userReqDTO {
    private int id;
    private Date entryDate;
    private Date procDate;
    private String procUsername;
    private String reqNickname;
    private String reqUsername;
    private String title;
    private String content;
    private boolean status;

}
