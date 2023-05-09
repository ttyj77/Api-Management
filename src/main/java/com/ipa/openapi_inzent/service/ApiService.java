package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.ApiDao;
import com.ipa.openapi_inzent.dao.UserDao;
import com.ipa.openapi_inzent.model.ApiDTO;
import com.ipa.openapi_inzent.model.ApisRoleDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {
    private ApiDao apiDao;

    public ApiService(@Qualifier("apiDao") ApiDao apiDao) {
        this.apiDao = apiDao;
    }

    public void insertApi(ApiDTO apiDTO) {
        apiDao.insertApi(apiDTO);
    }

    public void insertRole(ApisRoleDTO apisRoleDTO) {
        apiDao.insertRole(apisRoleDTO);
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

    public ApiDTO showRole() {
        return apiDao.showRole();
    }


}
