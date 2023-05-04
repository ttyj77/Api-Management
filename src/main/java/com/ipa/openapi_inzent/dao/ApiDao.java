package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.ApiDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface ApiDao {
    void insert(ApiDTO apiDTO) throws DataAccessException;

    List<ApiDTO> selectAll() throws DataAccessException;
    ApiDTO selectOne(int id) throws DataAccessException;

    void update(ApiDTO apiDTO) throws DataAccessException;

    void delete(int id) throws DataAccessException;
}
