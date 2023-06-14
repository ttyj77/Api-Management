package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.RoleDAO;
import com.ipa.openapi_inzent.model.RoleDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private RoleDAO roleDao;

    public RoleService(@Qualifier("roleDAO") RoleDAO roleDao) {
        this.roleDao = roleDao;
    }
    public void insert(RoleDTO roleDTO) {
        roleDao.insert(roleDTO);
    }

    public List<RoleDTO> selectAll() {
        return roleDao.selectAll();
    }

    public RoleDTO selectOne(int id) {
        return roleDao.selectOne(id);
    }

    public List<RoleDTO> selectApisRoleList(int id) {
        return roleDao.selectApisRoleList(id);
    }

    public void deleteRole(int id) {
        roleDao.deleteRole(id);
    }

    public List<RoleDTO> apisRoles() {
        return roleDao.apisRoles();
    }
}
