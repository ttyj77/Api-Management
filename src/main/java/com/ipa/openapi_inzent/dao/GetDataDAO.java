package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import javax.xml.crypto.Data;
import java.util.List;

@Mapper
public interface GetDataDAO {

    List<GetDataDTO> selectAllIndustry(String clientNum, String uri, String industry) throws DataAccessException;
    List<GetDataDTO> selectAll(String clientNum, String uri) throws DataAccessException;

    List<GetDataDTO> accountAll(String account, String clientNum, String uri) throws DataAccessException;

    List<GetDataDTO> accountOne(String account, String clientNum, String uri) throws DataAccessException;

    void deleteAccount(String org_code, String clientNum, String industry) throws DataAccessException;

    GetDataDTO getAccount(String clientNum, String uri, String orgCode) throws DataAccessException;

    List<DailyApiStatisticsDTO> dailyAPIStatistics() throws DataAccessException;

    DailyApiStatisticsDTO dailyAPIStatisticsOne(String date, String orgCode) throws DataAccessException;

    List<DailyApiSeqDTO> dailyApiSeq(String orgCode, String date) throws DataAccessException;

    List<DailyApiErrorDTO> dailyApiError(String orgCode, String date) throws DataAccessException;

    List<ErrorDTO> errorAll() throws DataAccessException;

    List<DailyApiStatisticsDTO> dailyTimeCall(String code, String date) throws DataAccessException;

    List<DailyApiStatisticsDTO> dailyStatisticsDate(String dday) throws DataAccessException;

    List<DailyApiStatisticsDTO> dailyStatisticsSearch(String keyword) throws DataAccessException;

    List<DailyApiStatisticsDTO> periodApiList() throws DataAccessException;

    List<DailyApiStatisticsDTO> periodStatisticsAll() throws DataAccessException;

    List<DailyApiStatisticsDTO> periodStatisticsCalendar(String start_date, String end_date) throws DataAccessException;

    DailyApiStatisticsDTO periodStatistics(String org_code, String start_date, String end_date) throws DataAccessException;

    List<DailyApiSeqDTO> periodResourceSeq(String orgCode, String start_date, String end_date) throws DataAccessException;

    List<DailyApiErrorDTO> periodApiResource(String orgCode, String start_date, String end_date) throws DataAccessException;

}
