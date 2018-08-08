package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Classify;
import java.util.List;

public interface ClassifyMapper {
    int deleteByPrimaryKey(String classid);

    int insert(Classify record);

    Classify selectByPrimaryKey(String classid);

    List<Classify> selectAll();

    int updateByPrimaryKey(Classify record);
}