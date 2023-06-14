package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.UserDao;
import com.ipa.openapi_inzent.model.AppUserDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import com.ipa.openapi_inzent.model.UserRoleDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private UserDao userDao;

    public UserService(PasswordEncoder passwordEncoder, @Qualifier("userDao") UserDao userDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    public int register(UserDTO userDTO) {
        System.out.println("UserService.register");
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // 정보 주체 번호 부여
        Random random = new Random();
        userDTO.setOwnNum(String.valueOf(random.nextInt(999999) + 1));
        userDao.register(userDTO);
        int id = userDTO.getId();
        return id;
    }

//    public Optional<UserDTO> findByProviderAndProviderId(String provider, String providerId) {
//
//        Optional<UserDTO> providerInfo = userDao.findByProviderAndProviderId(provider, providerId);
//        System.out.print("이건 뭘로 나오나 userservice" + userDao.findByProviderAndProviderId(provider, providerId));
//        return providerInfo;
//    }

    public List<UserDTO> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    //appuser 로그인 활용
    public AppUserDTO findByAppUsername(String username) {
        return userDao.findByAppUsername(username);
    }

    public UserDTO findByNickname(String nickname) {
        return userDao.findByNickname(nickname);
    }

    public List<UserDTO> selectOne(int id) {
        return userDao.selectOne(id);
    }

    public void delete(int id) {
        userDao.delete(id);
    }

    public void update(UserDTO userDTO) {
        userDao.update(userDTO);
    }

    public List<UserDTO> selectAll() {
        return userDao.selectAll();
    }

    public void insertRole(UserRoleDTO userRoleDTO) {
        userDao.insertRole(userRoleDTO);
    }

    public void deleteRole(int userId) {
        userDao.deleteRole(userId);
    }

    public List<UserDTO> userList() {
        return userDao.userList();
    }

    public UserDTO userOne(int id) {
        return userDao.userOne(id);
    }

    public List<UserRoleDTO> userRoles(int id) {
        return userDao.userRoles(id);
    }

    public List<UserRoleDTO> roleName(int id) {
        return userDao.roleName(id);
    }

    public List<UserDTO> accountListSearch(String keyword) {
        return userDao.accountListSearch(keyword);
    }

    public List<UserDTO> choiceActivate(boolean b) {
        return userDao.choiceActivate(b);
    }
}
