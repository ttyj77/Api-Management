package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccountInfoDTO {
    private Date issue_date;
    private String cnt;
    private String account_type;
    private String account_name;
    private String account_num;
}
