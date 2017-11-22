package com.beyond.algm.mapper;

import com.beyond.algm.model.AlgModelSet;
import com.beyond.algm.vo.AlgModelSetVo;

import java.util.List;

public interface AlgModelSetMapper {
    int deleteByPrimaryKey(String modelSetSn);

    int insert(AlgModelSet record);

    AlgModelSet selectByPrimaryKey(String modelSetSn);

    List<AlgModelSet> selectAll(String usrSn);

    int updateByPrimaryKey(AlgModelSet record);

    List<AlgModelSetVo> queryModelSet(String usrSn);

    int checkData(AlgModelSet algModelSet);

    String checkMaxOrderby(String usrSn);
}