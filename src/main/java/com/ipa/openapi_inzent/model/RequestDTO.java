package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class RequestDTO {
    private int id;
    private Date entryDate;
    private Date procDate;
    private String procUsername;
    private String title;
    private String content;
    private Boolean status;
    private int userId;

    // userDTO
    private String reqUsername;
    private String reqNickname;

}
