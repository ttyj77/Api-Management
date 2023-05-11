package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.ApiDetailsDao;
import com.ipa.openapi_inzent.model.ApiDetailsDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiDetailsService {
    private ApiDetailsDao apiDetailsDao;
    public ApiDetailsService(@Qualifier("apiDetailsDao") ApiDetailsDao apiDetailsDao){
        this.apiDetailsDao = apiDetailsDao;
    }

    public List<ApiDetailsDTO> selectAll(int id) {
        return apiDetailsDao.selectAll(id);
    }

    public ApiDetailsDTO selectOne(int id) {
        return apiDetailsDao.selectOne(id);
    }
}
