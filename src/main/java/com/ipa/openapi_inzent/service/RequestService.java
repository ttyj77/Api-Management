package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.RequestDao;
import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestService {
    private RequestDao requestDao;
    public RequestService(@Qualifier("requestDao") RequestDao requestDao) {
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
}