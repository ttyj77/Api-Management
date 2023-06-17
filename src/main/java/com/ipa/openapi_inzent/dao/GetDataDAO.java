package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.GetDataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface GetDataDAO {

    List<GetDataDTO> selectAll(String clientNum, String uri) throws DataAccessException;

    List<GetDataDTO> accountAll(String account, String clientNum, String uri) throws DataAccessException;

}