package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.ApiDTO;
import com.ipa.openapi_inzent.model.ApiDetailsDTO;
import com.ipa.openapi_inzent.model.ApisRoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface ApiDao {
    void insertApi(ApiDTO apiDTO) throws DataAccessException;

    void insertRole(ApisRoleDTO apisRoleDTO) throws DataAccessException;

    List<ApiDTO> selectAll() throws DataAccessException;

    ApiDTO selectOne(int id) throws DataAccessException;

    void update(ApiDTO apiDTO) throws DataAccessException;

    void updateRole(ApisRoleDTO apisRoleDTO) throws DataAccessException;

    List<ApiDTO> selectRoleList(int id) throws DataAccessException;

    void delete(int id) throws DataAccessException;

    ApiDTO showRole() throws DataAccessException;


    void deleteRole(int id) throws DataAccessException;

    List<ApiDTO> giveRole() throws DataAccessException;
}
