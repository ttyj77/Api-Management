package com.ipa.openapi_inzent.model;

import lombok.Data;

@Data
public class ResourceDTO {
    private int id;
    private int apisId;
    private boolean garbage;
    private int tagId;

    private String uri;
    private String summary;
}
