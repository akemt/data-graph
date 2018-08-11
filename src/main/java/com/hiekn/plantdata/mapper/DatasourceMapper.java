package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Datasource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DatasourceMapper {
    int deleteByPrimaryKey(String datasourceId);

    int insert(Datasource record);

    Datasource selectByPrimaryKey(String datasourceId);

    List<Datasource> selectAll();

    int updateByPrimaryKey(Datasource record);

    List<Map<String,Object>> getSynImportedDatasource(String classId);

    Datasource selectByClassIdAndKind(@Param("classId") String classId, @Param("kind") String kind);
}