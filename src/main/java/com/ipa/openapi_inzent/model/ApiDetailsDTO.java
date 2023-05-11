package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ApiDetailsDTO {
    private int id;
    private int resourceId;
    private String method;
    private String url;
    private String uri;
    private String summary;
    private String security;
    private String scope;
    private String operationId;
    private String version;
    private String status;
    private String authorization;
    private String tag;
    private boolean trash;
    // ApiDetailsDTO end

    //    private ResourceDTO resourceDTO;
    // 리소스 List 테이블
    private int resource_id;
    private int apisId;

}
