package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.MdAgencyDTO;
import com.ipa.openapi_inzent.model.MdServiceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface MydataDao {
    List<MdAgencyDTO> mdAgencySelectAll() throws DataAccessException;

    List<MdServiceDTO> mdServiceSelectModal(int id) throws DataAccessException;

    List<MdServiceDTO> mdServiceSelectList() throws DataAccessException;

    MdAgencyDTO mdAgencySelectOne(int id) throws DataAccessException;

    List<MdAgencyDTO> mdAgencySelectBox(String division) throws DataAccessException;

    void mdAgencyDelete(int id) throws DataAccessException;

    MdServiceDTO mdServiceSelectOne(int id) throws DataAccessException;

    List<MdServiceDTO> mdServiceSearchKeyword(String keyword) throws DataAccessException;
}
