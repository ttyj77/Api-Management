package com.ipa.openapi_inzent.service;

import com.ipa.openapi_inzent.dao.ApiDetailsDao;
import com.ipa.openapi_inzent.model.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ApiDetailsService {
    private ApiDetailsDao apiDetailsDao;

    public ApiDetailsService(@Qualifier("apiDetailsDao") ApiDetailsDao apiDetailsDao) {
        this.apiDetailsDao = apiDetailsDao;
    }

    public List<ApiDetailsDTO> selectAll() {
        return apiDetailsDao.selectAll();
    }

    public List<ResourceDTO> resourceList(int apisId) {
        return apiDetailsDao.resourceList(apisId);
    }

    public List<ApiDetailsDTO> resourceInAdList(int apisId) {
        return apiDetailsDao.resourceInAdList(apisId);
    }

    public ApiDetailsDTO selectOne(int id) {
        return apiDetailsDao.selectOne(id);
    }

    public List<ApiDetailsDTO> detailsList(int apisId) {
        return apiDetailsDao.detailsList(apisId);
    }


    public List<ResourceDTO> goTrashResource() {
        return apiDetailsDao.goTrashResource();
    }

    public List<ApiDetailsDTO> goTrashDetail() {
        return apiDetailsDao.goTrashDetail();
    }

    public void completeDelete(int id) {
        apiDetailsDao.completeDelete(id);
    }

    public void updateDetail(ApiDetailsDTO apiDetailsDTO) {
        apiDetailsDao.updateDetail(apiDetailsDTO);
    }

    public void updateResource(ResourceDTO resourceDTO) {
        apiDetailsDao.updateResource(resourceDTO);
    }

    public ResourceDTO resourceOne(int id) {
        return apiDetailsDao.resourceOne(id);
    }

    public void resourceDelete(int id) {
        apiDetailsDao.resourceDelete(id);
    }

    public List<TagDTO> selectAllTag() {
        return apiDetailsDao.selectAllTag();
    }

    public List<ApiDetailsDTO> searchPath(String keyword, int apisId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("keyword", keyword);
        map.put("apisId", apisId);
        return apiDetailsDao.searchPath(map);
    }

    public int insertResource(ResourceDTO resourceDTO) {
        System.out.println("ApiDetailsService.insertResource");
        System.out.println("resourceDTO = " + resourceDTO);
        apiDetailsDao.insertResource(resourceDTO);
        int id = resourceDTO.getId();
        return id;
    }

    public int insertApiDetail(ApiDetailsDTO apiDetailsDTO) {
        apiDetailsDao.insertApiDetail(apiDetailsDTO);
        int id = apiDetailsDTO.getId();
        System.out.println("apiDetailsId" + id);
        return id;
    }

    public ApiDetailsDTO searchDetail(int id) {
        return apiDetailsDao.searchDetail(id);
    }

    public List<ApiDetailsDTO> trashSearch(String keyword) {
        return apiDetailsDao.trashSearch(keyword);
    }

    public void insertParameter(ParameterDTO parameterDTO) {
        apiDetailsDao.insertParameter(parameterDTO);
    }

    public List<ParameterDTO> searchParameter(int id) {
        return apiDetailsDao.searchParameter(id);
    }

    public int insertResponse(ResponseDTO responseDTO) {
        apiDetailsDao.insertResponse(responseDTO);
        return responseDTO.getId();
    }

    public void insertResParam(ResParamDTO resParamDTO) {
        apiDetailsDao.insertResParam(resParamDTO);
    }
}
