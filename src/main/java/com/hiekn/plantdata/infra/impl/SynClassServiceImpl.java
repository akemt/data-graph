package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.Entity.Code;
import com.hiekn.plantdata.infra.SynClassService;
import com.hiekn.plantdata.mapper.ClassifyMapper;
import com.hiekn.plantdata.mapper.CodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class SynClassServiceImpl implements SynClassService {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private CodeMapper codeMapper;



    @Override
    public List<Map<String,Object>> getClassList(){

    return classifyMapper.selectAll();
    }

    public Map<String,Object> getCodeList(String classId,String searchStr,int page){
        Map<String,Object> map = new HashMap<>();
        int pageSize = 10;
        //一页显示10条
        map.put("pageSize",pageSize);
        int count = codeMapper.getCodeCountByClass(classId);
        map.put("pageCount",(count-1)/pageSize+1);
        map.put("page",page);
        //查询条件
        Map<String,Object> condition = new HashMap<>();
        condition.put("classId",classId);
        condition.put("searchStr",searchStr);
        condition.put("startNum",(page-1)*pageSize+1);
        condition.put("endNum",page*pageSize);
        List<Code> resultList = codeMapper.getCodeListByClassId(condition);
        map.put("define",resultList);

        return map;

    }
}
