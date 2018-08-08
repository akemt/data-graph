package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.Entity.Synonym;
import com.hiekn.plantdata.infra.SynClassService;
import com.hiekn.plantdata.infra.SynonymService;
import com.hiekn.plantdata.mapper.ClassifyMapper;
import com.hiekn.plantdata.mapper.DatasourceMapper;
import com.hiekn.plantdata.mapper.SynonymMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class SynonymServiceImpl implements SynonymService {

    @Autowired
    private SynonymMapper synonymMapper;

    @Autowired
    private DatasourceMapper datasourceMapper;


    @Override
    public List<Map<String,Object>> getSynListByCodeId(String codeId){

        return synonymMapper.getSynListByCodeId(codeId);

    }

    @Override
    public List<Map<String,Object>> getSynImportedDatasourece(String classId){

        return datasourceMapper.getSynImportedDatasourece(classId);

    }

    @Override
    public int manualAddSyn(Synonym synonym){
        return synonymMapper.insert(synonym);
    }

    @Override
    public int manualUpdateSyn(Synonym synonym){
        return synonymMapper.updateByPrimaryKey(synonym);
    }

    @Override
    public int deleteSyn(String synId){
        return synonymMapper.deleteByPrimaryKey(synId);
    }


}
