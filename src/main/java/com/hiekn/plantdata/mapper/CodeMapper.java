package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Code;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeMapper {
    int deleteByPrimaryKey(String codeid);

    int insert(Code record);

    Code selectByPrimaryKey(String codeid);

    List<Code> selectAll();

    int updateByPrimaryKey(Code record);
}