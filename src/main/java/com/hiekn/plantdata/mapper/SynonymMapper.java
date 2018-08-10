package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Synonym;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SynonymMapper {
    int deleteByPrimaryKey(String synId);

    int insert(Synonym record);

    Synonym selectByPrimaryKey(String synid);

    List<Synonym> selectAll();

    int updateByPrimaryKey(Synonym record);

    List<Map<String,Object>> getSynListByCodeId(String codeId);

    int getSynCountByName(String searchStr);

    List<Map<String,Object>> searchCodeNameInfo(String searchStr);

    List<Map<String,Object>> searchSynNameInfo(String searchStr);
}