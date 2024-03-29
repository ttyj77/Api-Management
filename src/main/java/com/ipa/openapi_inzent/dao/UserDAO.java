package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.AppUserDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.model.UserRoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface UserDAO {
    void register(UserDTO userDTO);

    List<UserDTO> findByUsername(String username) throws DataAccessException;

    UserDTO findByNickname(String nickname) throws DataAccessException;

    List<UserDTO> selectOne(int id) throws DataAccessException;

    void delete(int id) throws DataAccessException;

    void update(UserDTO userDTO) throws DataAccessException;

    List<UserDTO> selectAll() throws DataAccessException;

    void insertRole(UserRoleDTO userRoleDTO) throws DataAccessException;

    List<UserDTO> userList() throws DataAccessException;

    UserDTO userOne(int id) throws DataAccessException;

    List<UserRoleDTO> userRoles(int id) throws DataAccessException;


    List<UserRoleDTO> roleName(int id) throws DataAccessException;

    void deleteRole(int userId) throws DataAccessException;

    List<UserDTO> accountListSearch(String keyword) throws DataAccessException;

    List<UserDTO> choiceActivate(boolean b) throws DataAccessException;

    /* app user */
    AppUserDTO findByAppUsername(String username) throws DataAccessException;

    void appRegister(AppUserDTO appUserDTO) throws DataAccessException;

    String findToken(int id) throws DataAccessException;

    void tokenExpireDate(int id) throws DataAccessException;
}
