package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class InvestTransactionDTO {
    private String base_amt;
    private String prod_code;
    private Date trans_dtime;
    private String prod_name;
    private String trans_num;
    private String trans_type_detail;
    private String trans_amt;
    private String currency_code;
    private String balance_amt;
    private String trans_type;
    private String settle_amt;
}
