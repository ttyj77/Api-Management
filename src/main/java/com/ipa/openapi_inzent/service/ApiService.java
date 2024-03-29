package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.ApiDAO;
import com.ipa.openapi_inzent.model.ApiDTO;
import com.ipa.openapi_inzent.model.ApisRoleDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {
    private ApiDAO apiDao;

    public ApiService(@Qualifier("apiDAO") ApiDAO apiDao) {
        this.apiDao = apiDao;
    }

    public int insertApi(ApiDTO apiDTO) {
        apiDao.insertApi(apiDTO);
        return apiDTO.getId();
    }

    public void insertRole(ApisRoleDTO apisRoleDTO) {
        apiDao.insertRole(apisRoleDTO);
    }

    public void update(ApiDTO apiDTO) {
        apiDao.update(apiDTO);
    }

    public void updateRole(ApisRoleDTO apisRoleDTO) {
        apiDao.updateRole(apisRoleDTO);
    }

    public List<ApiDTO> selectRoleList(int id) {
        return apiDao.selectRoleList(id);
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


    public void deleteRole(int id) {
        apiDao.deleteRole(id);
    }

    public List<ApiDTO> giveRole() {
        return apiDao.giveRole();
    }
}
