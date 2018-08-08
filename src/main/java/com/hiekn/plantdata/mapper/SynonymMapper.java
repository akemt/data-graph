package com.hiekn.plantdata.mapper;

import com.hiekn.plantdata.Entity.Synonym;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SynonymMapper {
    int deleteByPrimaryKey(String synid);

    int insert(Synonym record);

    Synonym selectByPrimaryKey(String synid);

    List<Synonym> selectAll();

    int updateByPrimaryKey(Synonym record);
}