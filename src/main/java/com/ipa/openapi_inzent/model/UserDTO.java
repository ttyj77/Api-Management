package com.ipa.openapi_inzent.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String token;
    private Date issueDate;
    private Date expireDate;

    private String email;
    private boolean activate;
    private Date createDate;
    private boolean approve; // false : 요청 상태 / true : 승인 완료
    private String ownNum;

    // UserRole
    private int userId;
    private int roleId;

    private List<String> roleList;
    private String role;

    // role
    private String code;
    private String name;



}
