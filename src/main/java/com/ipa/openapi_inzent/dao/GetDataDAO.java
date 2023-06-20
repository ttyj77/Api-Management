package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.GetDataDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface GetDataDAO {

    List<GetDataDTO> selectAllIndustry(String clientNum, String uri, String industry) throws DataAccessException;
    List<GetDataDTO> selectAll(String clientNum, String uri) throws DataAccessException;

    List<GetDataDTO> accountAll(String account, String clientNum, String uri) throws DataAccessException;

    List<GetDataDTO> accountOne(String account, String clientNum, String uri) throws DataAccessException;

    void deleteAccount(String org_code, String clientNum, String industry) throws DataAccessException;

    GetDataDTO getAccount(String clientNum, String uri, String orgCode) throws DataAccessException;

}
