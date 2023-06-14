package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.MydataDAO;
import com.ipa.openapi_inzent.model.*;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MydataService {

    private MydataDAO mydataDao;

    public MydataService(MydataDAO mydataDao) {
        this.mydataDao = mydataDao;
    }

    public List<MdAgencyDTO> mdAgencySelectAll() {
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


    ///////////////////////////////////////////////////////////////////////////////////////////
    //                            (oﾟvﾟ)ノ  Provider Page  (oﾟvﾟ)ノ                           //
    ///////////////////////////////////////////////////////////////////////////////////////////
    public List<MdProviderDTO> mdProviderSelectAll() {
        List<MdProviderDTO> list = mydataDao.mdProviderSelectAll();
        SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

        Calendar cal = Calendar.getInstance();

        try {
            System.out.println("MydataService.mdProviderSelectAll");
            for (MdProviderDTO m : list) {
                Date forMatDate = dfFormat.parse(m.getReqDate());
                m.setReqDate(newFormat.format(forMatDate)); //요청일 ex) 2021-11-23
                m.setReqTime(timeFormat.format(forMatDate)); //요청시간 ex) 15:18:16.614
                cal.setTime(forMatDate);
                cal.add(Calendar.MILLISECOND, m.getRuntime()); // 요청일에 응답시간(runtime)을 더함 => 응답일자
                m.setResDate(dfFormat.format(cal.getTime())); //응답일자 ex) 2021-11-23 15:18:16.642

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MdProviderDTO> mdReqList() {
        List<MdProviderDTO> list = mydataDao.mdReqList();
        SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat hmsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

        Calendar cal = Calendar.getInstance();

        try {
            System.out.println("MydataService.mdProviderSelectAll");
            for (MdProviderDTO m : list) {
                Date forMatDate = dfFormat.parse(m.getReqDate());
                m.setReqDate(newFormat.format(forMatDate)); //요청일 ex) 2021-11-23
                m.setReqTime(timeFormat.format(forMatDate)); //요청시간 ex) 15:18:16.614
                cal.setTime(forMatDate);
                cal.add(Calendar.MILLISECOND, m.getRuntime()); // 요청일에 응답시간(runtime)을 더함 => 응답일자
                m.setResDate(dfFormat.format(cal.getTime())); //응답일자 ex) 2021-11-23 15:18:16.642
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void mdProviderInsert(MdProviderDTO mdProviderDTO) {
        mydataDao.mdProviderInsert(mdProviderDTO);
    }

    public MdProviderDTO mdProviderSelectOne(int id) {
        return mydataDao.mdProviderSelectOne(id);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //                            (oﾟvﾟ)ノ  Collector Page  (oﾟvﾟ)ノ                           //
    ///////////////////////////////////////////////////////////////////////////////////////////
    public List<MdCollectorDTO> mdCollectorSelectAll() {
        List<MdCollectorDTO> list = mydataDao.mdCollectorSelectAll();
        SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

        Calendar cal = Calendar.getInstance();

        try {
            for (MdCollectorDTO m : list) {
                Date forMatDate = dfFormat.parse(m.getReqDate());
                m.setReqDate(newFormat.format(forMatDate)); //요청일 ex) 2021-11-23
                m.setReqTime(timeFormat.format(forMatDate)); //요청시간 ex) 15:18:16.614
                cal.setTime(forMatDate);
                cal.add(Calendar.MILLISECOND, m.getRuntime()); // 요청일에 응답시간(runtime)을 더함 => 응답일자
                m.setResDate(dfFormat.format(cal.getTime())); //응답일자 ex) 2021-11-23 15:18:16.642
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<MdProviderDTO> mdProviderCustomerList(String customerNum) {
        List<MdProviderDTO> list = mydataDao.mdProviderCustomerList(customerNum);
        SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

        Calendar cal = Calendar.getInstance();

        try {
            for (MdProviderDTO m : list) {
                Date forMatDate = dfFormat.parse(m.getReqDate());
                m.setReqDate(newFormat.format(forMatDate)); //요청일 ex) 2021-11-23
                m.setReqTime(timeFormat.format(forMatDate)); //요청시간 ex) 15:18:16.614
                cal.setTime(forMatDate);
                cal.add(Calendar.MILLISECOND, m.getRuntime()); // 요청일에 응답시간(runtime)을 더함 => 응답일자
                m.setResDate(dfFormat.format(cal.getTime())); //응답일자 ex) 2021-11-23 15:18:16.642
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<MdTokenDTO> mdAstList() {
        return mydataDao.mdAstList();
    }

    public MdAgencyDTO mdAstOne(int id) {
        return mydataDao.mdAstOne(id);
    }

    public List<MdTokenDTO> mdTokenSearch(String keyword) {
        return mydataDao.mdTokenSearch(keyword);
    }


    public List<MdReqInfoDTO> mdReqAll() {
        return mydataDao.mdReqAll();
    }

    public List<MdReqInfoDTO> mdReqSearch(String keyword) {
        return mydataDao.mdReqSearch(keyword);
    }

    public List<MdProviderDTO> mdProviderSearch(String keyword) {
        List<MdProviderDTO> list = mydataDao.mdProviderSearch(keyword);
        SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");

        Calendar cal = Calendar.getInstance();

        try {
            System.out.println("MydataService.mdProviderSelectAll");
            for (MdProviderDTO m : list) {
                Date forMatDate = dfFormat.parse(m.getReqDate());
                m.setReqDate(newFormat.format(forMatDate)); //요청일 ex) 2021-11-23
                m.setReqTime(timeFormat.format(forMatDate)); //요청시간 ex) 15:18:16.614
                cal.setTime(forMatDate);
                cal.add(Calendar.MILLISECOND, m.getRuntime()); // 요청일에 응답시간(runtime)을 더함 => 응답일자
                m.setResDate(dfFormat.format(cal.getTime())); //응답일자 ex) 2021-11-23 15:18:16.642
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;

    }
}

