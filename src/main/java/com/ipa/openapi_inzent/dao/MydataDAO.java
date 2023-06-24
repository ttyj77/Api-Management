package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface MydataDAO {
    List<MdAgencyDTO> mdAgencySelectAll() throws DataAccessException;

    List<MdServiceDTO> mdServiceSelectModal(int id) throws DataAccessException;

    List<MdServiceDTO> mdServiceSelectList() throws DataAccessException;

    MdAgencyDTO mdAgencySelectOne(int id) throws DataAccessException;

    List<MdAgencyDTO> mdAgencySelectBox(String division) throws DataAccessException;

    void mdAgencyDelete(int id) throws DataAccessException;

    MdServiceDTO mdServiceSelectOne(int id) throws DataAccessException;

    List<MdServiceDTO> mdServiceSearchKeyword(String keyword) throws DataAccessException;

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> mdProvider page

    List<MdProviderDTO> mdProviderSelectAll() throws DataAccessException;

    MdProviderDTO mdProviderSelectOne(int id) throws DataAccessException;

    List<MdTokenDTO> mdAstList() throws DataAccessException;

    MdAgencyDTO mdAstOne(int id) throws DataAccessException;

    List<MdTokenDTO> mdTokenSearch(String keyword) throws DataAccessException;

    void mdProviderInsert(MdProviderDTO mdProviderDTO) throws DataAccessException;

    List<MdCollectorDTO> mdCollectorSelectAll() throws DataAccessException;

    List<MdProviderDTO> mdReqList() throws DataAccessException;

    List<MdProviderDTO> mdProviderCustomerList(MdProviderDTO mdProviderDTO) throws DataAccessException;

    List<MdReqInfoDTO> mdReqAll() throws DataAccessException;

    List<MdReqInfoDTO> mdReqSearch(String keyword) throws DataAccessException;

    List<MdProviderDTO> mdProviderSearch(String keyword) throws DataAccessException;

    List<MdAgencyDTO> agencyIndustry(String industry) throws DataAccessException;

    List<MdAgencyDTO> mdAgencyService() throws DataAccessException;

    List<MdAgencyDTO> mdAgencyServiceOne(String orgCode) throws DataAccessException;

    MdAgencyDTO mdAgencyCode(String orgCode) throws DataAccessException;

    int selectReqInfoId(String code,String clientNum) throws DataAccessException;

    MdAgencyDTO mdAstOrgCode(String code) throws DataAccessException;
}
