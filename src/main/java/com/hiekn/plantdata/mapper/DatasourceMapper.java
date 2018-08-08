package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Datasource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceMapper {
    int deleteByPrimaryKey(String datasourceid);

    int insert(Datasource record);

    Datasource selectByPrimaryKey(String datasourceid);

    List<Datasource> selectAll();

    int updateByPrimaryKey(Datasource record);
}