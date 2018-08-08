package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Classify;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClassifyMapper {
    int deleteByPrimaryKey(String classid);

    int insert(Classify record);

    Classify selectByPrimaryKey(String classid);

    List<Map<String,Object>> selectAll();

    int updateByPrimaryKey(Classify record);



}