package com.beyond.algo.mapper;

import com.beyond.algo.model.AlgRModuleTag;
import java.util.List;

public interface AlgRModuleTagMapper {
    int deleteByPrimaryKey(String sn);

    int insert(AlgRModuleTag record);

    AlgRModuleTag selectByPrimaryKey(String sn);

    List<AlgRModuleTag> selectAll();

    int updateByPrimaryKey(AlgRModuleTag record);
}