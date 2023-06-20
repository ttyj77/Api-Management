package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.GetDataDAO;
import com.ipa.openapi_inzent.model.GetDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetDataService {
    private GetDataDAO getDataDao;

    public GetDataService(GetDataDAO getDataDao) {
        this.getDataDao = getDataDao;
    }

    public List<GetDataDTO> selectAll(String clientNum, String uri) {
        return getDataDao.selectAll(clientNum, uri);
    }
    public List<GetDataDTO> selectAllIndustry(String clientNum, String uri, String industry) {
        return getDataDao.selectAllIndustry(clientNum, uri, industry);
    }

    public List<GetDataDTO> accountAll(String account, String clientNum, String uri) {
        return getDataDao.accountAll(account, clientNum , uri);
    }

    public List<GetDataDTO> accountOne(String account, String clientNum, String uri) {
        return getDataDao.accountOne(account, clientNum , uri);
    }

    public void deleteAccount(String org_code, String ownNum, String industry) {
        getDataDao.deleteAccount(org_code, ownNum, industry);
    }

    public GetDataDTO getAccount(String clientNum, String uri, String orgCode) {

        return getDataDao.getAccount(clientNum, uri, orgCode);
    }
}
