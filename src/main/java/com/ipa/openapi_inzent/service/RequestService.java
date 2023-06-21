package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.RequestDAO;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private RequestDAO requestDao;
    public RequestService(@Qualifier("requestDAO") RequestDAO requestDao) {
        this.requestDao = requestDao;
    }

    public List<RequestDTO> selectAll() {
        return requestDao.selectAll();
    }

    public List<RequestDTO> reqUserList() {
        return requestDao.reqUserList();
    }

    public RequestDTO selectOne(int id) {
        return requestDao.selectOne(id);
    }

    public void insert(RequestDTO requestDTO) {
        requestDao.insert(requestDTO);
    }

    public void delete(int id) {
        requestDao.delete(id);
    }

    public RequestDTO selectUserId(int userId) {
        return requestDao.selectUserId(userId);
    }

    public void updateRequest(RequestDTO requestDTO) {
        requestDao.updateRequest(requestDTO);
    }

    public List<RequestDTO> requestSearch(String keyword) {
        return requestDao.requestSearch(keyword);
    }

    public UserDTO selectToken(String ownNum){
        return requestDao.selectToken(ownNum);
    }
}
