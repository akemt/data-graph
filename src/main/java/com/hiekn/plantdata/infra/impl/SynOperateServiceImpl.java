package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.Entity.Code;
import com.hiekn.plantdata.infra.SynClassService;
import com.hiekn.plantdata.infra.SynOperateService;
import com.hiekn.plantdata.mapper.NomatchMapper;
import com.hiekn.plantdata.mapper.SynonymMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SynOperateServiceImpl implements SynOperateService {

    @Autowired
    private NomatchMapper nomatchMapper;

    public List<Map<String,Object>> getNomatchList(){
        return nomatchMapper.getNomatchList();
    }

    public Map<String,Object> getNomatchInfo(String classId,int page){

        Map<String,Object> map = new HashMap<>();
        int pageSize = 10;
        //一页显示10条
        map.put("pageSize",pageSize);
        int count = nomatchMapper.getNomatchInfoCountByClass(classId);
        map.put("pageCount",(count-1)/pageSize+1);
        map.put("page",page);

        //查询条件
        Map<String,Object> condition = new HashMap<>();
        condition.put("classId",classId);
        condition.put("startNum",(page-1)*pageSize+1);
        condition.put("endNum",page*pageSize);
        List<Map<String,Object>> resultList = nomatchMapper.getNomatchInfoList(condition);
        map.put("define",resultList);

        return map;

    }

}
