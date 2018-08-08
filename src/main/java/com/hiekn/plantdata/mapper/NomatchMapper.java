package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Nomatch;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NomatchMapper {
    int deleteByPrimaryKey(String nomatchid);

    int insert(Nomatch record);

    Nomatch selectByPrimaryKey(String nomatchid);

    List<Nomatch> selectAll();

    int updateByPrimaryKey(Nomatch record);
}