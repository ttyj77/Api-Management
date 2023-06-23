package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.RequestDTO;
import com.ipa.openapi_inzent.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface RequestDAO {
    List<RequestDTO> selectAll() throws DataAccessException;

    List<RequestDTO> reqUserList() throws DataAccessException;

    void insert(RequestDTO requestDTO) throws DataAccessException;

    RequestDTO selectOne(int id) throws DataAccessException;

    void delete(int id) throws DataAccessException;

    RequestDTO selectUserId(int userId) throws DataAccessException;

    void updateRequest(RequestDTO requestDTO) throws DataAccessException;

    List<RequestDTO> requestSearch(String keyword) throws DataAccessException;


}
