package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionDataDTO {
    private String trans_amt;
    private String trans_type;
    private String balance_amt;
    private String trans_class;
    private Date trans_dtime;
    private String currency_code;

}
