package com.ipa.openapi_inzent.dao;

import com.ipa.openapi_inzent.model.ApiDetailsDTO;
import com.ipa.openapi_inzent.model.ResourceDTO;
import com.ipa.openapi_inzent.model.TagDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface ApiDetailsDao {
    List<ApiDetailsDTO> selectAll() throws DataAccessException;

    List<ResourceDTO> resourceList(int apisId) throws DataAccessException;

    List<ApiDetailsDTO> resourceInAdList(int apisId) throws DataAccessException;

    ApiDetailsDTO selectOne(int id) throws DataAccessException;


    List<ApiDetailsDTO> detailsList(int apisId) throws DataAccessException;

    List<ResourceDTO> goTrashResource() throws DataAccessException;

    List<ApiDetailsDTO> goTrashDetail() throws DataAccessException;

    void completeDelete(int id) throws DataAccessException;

    void updateDetail(ApiDetailsDTO apiDetailsDTO)throws DataAccessException;

    void updateResource(ResourceDTO resourceDTO)throws DataAccessException;

    ResourceDTO resourceOne(int id) throws DataAccessException;

    void resourceDelete(int id) throws DataAccessException;

    List<TagDTO> selectAllTag() throws DataAccessException;
}
