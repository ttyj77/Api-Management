package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.ApiDetailsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface ApiDetailsDao {
    List<ApiDetailsDTO> selectAll(int id) throws DataAccessException;
    ApiDetailsDTO selectOne(int id) throws DataAccessException;
}
