package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Code;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CodeMapper {
    int deleteByPrimaryKey(String codeid);

    int insert(Code record);

    Code selectByPrimaryKey(String codeid);

    List<Code> selectAll();

    int updateByPrimaryKey(Code record);

    int getCodeCountByClass(String classId);

    List<Code> getCodeListByClassId(Map<String,Object> condition);

    int getCodeCountByName(String searchStr);


}