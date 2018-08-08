package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Code;
import java.util.List;

public interface CodeMapper {
    int deleteByPrimaryKey(String codeid);

    int insert(Code record);

    Code selectByPrimaryKey(String codeid);

    List<Code> selectAll();

    int updateByPrimaryKey(Code record);
}