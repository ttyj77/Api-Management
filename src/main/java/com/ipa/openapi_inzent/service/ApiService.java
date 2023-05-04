package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.ApiDao;
import com.ipa.openapi_inzent.dao.UserDao;
import com.ipa.openapi_inzent.model.ApiDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {
    private ApiDao apiDao;

    public ApiService(@Qualifier("apiDao") ApiDao apiDao) {
        this.apiDao = apiDao;
    }

    public void insert(ApiDTO apiDTO) {
        apiDao.insert(apiDTO);
    }

    public void update(ApiDTO apiDTO) {
        apiDao.update(apiDTO);
    }

    public List<ApiDTO> selectAll() {
        return apiDao.selectAll();
    }

    public ApiDTO selectOne(int id) {
        return apiDao.selectOne(id);
    }

    public void delete(int id) {
        apiDao.delete(id);

    }

}
