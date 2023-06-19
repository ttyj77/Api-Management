package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.RoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface RoleDAO {
    void insert(RoleDTO roleDTO) throws DataAccessException;

    List<RoleDTO> selectAll() throws DataAccessException;

    RoleDTO selectOne(int id) throws DataAccessException;

    List<RoleDTO> selectApisRoleList(int id) throws DataAccessException;

    void deleteRole(int id) throws DataAccessException;

    List<RoleDTO> apisRoles() throws DataAccessException;
}
