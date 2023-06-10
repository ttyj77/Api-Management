package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ApiDetailsDao {
    List<ApiDetailsDTO> selectAll() throws DataAccessException;

    List<ResourceDTO> resourceList(int apisId) throws DataAccessException;

    List<ApiDetailsDTO> resourceInAdList(int apisId) throws DataAccessException;

    ApiDetailsDTO selectOne(int id) throws DataAccessException;


    List<ApiDetailsDTO> detailsList(int apisId) throws DataAccessException;

    List<ResourceDTO> goTrashResource() throws DataAccessException;

    List<ResourceDTO> resourceTrashList() throws DataAccessException;

    List<ApiDetailsDTO> goTrashDetail() throws DataAccessException;

    void completeDelete(int id) throws DataAccessException;

    void updateDetail(ApiDetailsDTO apiDetailsDTO) throws DataAccessException;

    void updateResource(ResourceDTO resourceDTO) throws DataAccessException;

    ResourceDTO resourceOne(int id) throws DataAccessException;

    void resourceDelete(int id) throws DataAccessException;

    List<TagDTO> selectAllTag() throws DataAccessException;

    List<ApiDetailsDTO> searchPath(HashMap<String, Object> map) throws DataAccessException;

    void insertResource(ResourceDTO resourceDTO) throws DataAccessException;

    void insertApiDetail(ApiDetailsDTO apiDetailsDTO) throws DataAccessException;

    ApiDetailsDTO searchDetail(int id) throws DataAccessException;

    List<ApiDetailsDTO> trashSearch(String keyword) throws DataAccessException;

    void insertParameter(ParameterDTO parameterDTO) throws DataAccessException;

    List<ParameterDTO> searchParameter(int id) throws DataAccessException;

    void insertResponse(ResponseDTO responseDTO) throws DataAccessException;

    void insertResParam(ResParamDTO resParamDTO) throws DataAccessException;

    // details 아이디로 해당 응답 코드 출력
    List<ResponseDTO> selectResponseList(int id) throws DataAccessException;

    // 응답코드 아이디로 해당하는 파라미터들 출력
    List<ResParamDTO> selectResParamList(int id) throws DataAccessException;

    void removeResCode(int id) throws DataAccessException;

    void removeResParam(int id) throws DataAccessException;
}
