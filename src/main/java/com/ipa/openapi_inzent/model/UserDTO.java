package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String token;
    private String email;
    private boolean activate;
    private Date createDate;
    private String role;
}
