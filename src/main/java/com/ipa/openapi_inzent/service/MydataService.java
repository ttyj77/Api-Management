package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.MydataDao;
import com.ipa.openapi_inzent.model.MdAgencyDTO;
import com.ipa.openapi_inzent.model.MdServiceDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MydataService {

    private MydataDao mydataDao;

    public MydataService(MydataDao mydataDao) {
        this.mydataDao = mydataDao;
    }

    public List<MdAgencyDTO> mdAgencySelectAll() {
        System.out.println("MydataService.mdAgencySelectAll >> " + mydataDao.mdAgencySelectAll());
        return mydataDao.mdAgencySelectAll();
    }

    public List<MdServiceDTO> mdServiceSelectAll(int id) {

        return mydataDao.mdServiceSelectAll(id);
    }

    public MdAgencyDTO mdAgencySelectOne(int id) {
        return mydataDao.mdAgencySelectOne(id);
    }

    public List<MdAgencyDTO> mdAgencySelectBox(String division) {
        return mydataDao.mdAgencySelectBox(division);
    }
}
