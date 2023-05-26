package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.RoleDao;
import com.ipa.openapi_inzent.model.RoleDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

@Service
public class RoleService {
    private RoleDao roleDao;

    public RoleService(@Qualifier("roleDao") RoleDao roleDao) {
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
}
