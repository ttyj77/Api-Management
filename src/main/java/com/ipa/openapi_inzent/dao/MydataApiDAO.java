package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.DataDTO;
import com.ipa.openapi_inzent.model.GetDataDTO;
import com.ipa.openapi_inzent.model.MdProviderDTO;
import com.ipa.openapi_inzent.model.MdReqInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

@Mapper
public interface MydataApiDAO {
    void providerHistoryInsert(MdProviderDTO mdProviderDTO) throws DataAccessException;

    void reqInfoInsert(MdReqInfoDTO mdReqInfoDTO) throws DataAccessException;

    void dataInsert(GetDataDTO getDataDTO) throws DataAccessException;
}
