package com.ipa.openapi_inzent.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class AppUserDTO {
    public int id;
    public String username;
    public String nickname;
    public Date createDate;
    public List<String> roleList;
    public String role;

    public String password;


}
