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

    /* mdAgency 테이블 - modal - mdService 상세 */
    public List<MdServiceDTO> mdServiceSelectModal(int id) {
        return mydataDao.mdServiceSelectModal(id);
    }

    /* 마이데이터 서비스 관리 페이지 */
    public List<MdServiceDTO> mdServiceSelectList() {

        return mydataDao.mdServiceSelectList();
    }

    public MdAgencyDTO mdAgencySelectOne(int id) {
        return mydataDao.mdAgencySelectOne(id);
    }

    public List<MdAgencyDTO> mdAgencySelectBox(String division) {
        return mydataDao.mdAgencySelectBox(division);
    }

    public void mdAgencyDelete(int id) {
        mydataDao.mdAgencyDelete(id);
    }


    public MdServiceDTO mdServiceSelectOne(int id) {
        return mydataDao.mdServiceSelectOne(id);
    }

    public List<MdServiceDTO> mdServiceSearchKeyword(String keyword) {
        return mydataDao.mdServiceSearchKeyword(keyword);
    }
}

