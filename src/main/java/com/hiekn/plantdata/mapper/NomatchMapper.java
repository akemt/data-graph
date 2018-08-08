package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Nomatch;
import java.util.List;

public interface NomatchMapper {
    int deleteByPrimaryKey(String nomatchid);

    int insert(Nomatch record);

    Nomatch selectByPrimaryKey(String nomatchid);

    List<Nomatch> selectAll();

    int updateByPrimaryKey(Nomatch record);
}