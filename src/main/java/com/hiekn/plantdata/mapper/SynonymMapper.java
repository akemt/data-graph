package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Synonym;
import java.util.List;

public interface SynonymMapper {
    int deleteByPrimaryKey(String synid);

    int insert(Synonym record);

    Synonym selectByPrimaryKey(String synid);

    List<Synonym> selectAll();

    int updateByPrimaryKey(Synonym record);
}