package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.GetDataDAO;
import com.ipa.openapi_inzent.dao.MydataApiDAO;
import com.ipa.openapi_inzent.dao.MydataDAO;
import com.ipa.openapi_inzent.model.DataDTO;
import com.ipa.openapi_inzent.model.MdProviderDTO;
import com.ipa.openapi_inzent.model.MdReqInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class MydataApiService {

    private MydataApiDAO mydataApiDAO;

    public MydataApiService(MydataApiDAO mydataApiDAO) {
        this.mydataApiDAO = mydataApiDAO;
    }

    public void providerHistoryInsert(MdProviderDTO mdProviderDTO) {
        System.out.println("MydataApiService.providerHistoryInsert");
        System.out.println("dataDTO = " + mdProviderDTO);
        mydataApiDAO.providerHistoryInsert(mdProviderDTO);
    }

    public int reqInfoInsert(MdReqInfoDTO mdReqInfoDTO) {
        System.out.println("MydataApiService.reqInfoInsert");
        System.out.println(mdReqInfoDTO);
        mydataApiDAO.reqInfoInsert(mdReqInfoDTO);
        return mdReqInfoDTO.getId();
    }
}
