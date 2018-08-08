package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.infra.SynClassService;
import com.hiekn.plantdata.mapper.ClassifyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class SynClassServiceImpl implements SynClassService {

    @Autowired
    private ClassifyMapper classifyMapper;



    @Override
    public List<Map<String,Object>> getClassList(){

    return classifyMapper.selectAll();
    }

}
