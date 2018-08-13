package com.hiekn.plantdata.infra.impl;

import com.alibaba.fastjson.JSONArray;
import com.hiekn.plantdata.Entity.Nomatch;
import com.hiekn.plantdata.Entity.Synonym;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.infra.SynMatchService;
import com.hiekn.plantdata.mapper.NomatchMapper;
import com.hiekn.plantdata.mapper.SynonymMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class SynMatchServiceImpl implements SynMatchService {

    @Autowired
    private NomatchMapper nomatchMapper;
    @Autowired
    private SynonymMapper synonymMapper;

    //获得左侧列表+搜索
    public List<Nomatch> getMatchCodeList(String datasourceId){

        //查询条件
        Map<String,Object> condition = new HashMap<>();
        condition.put("datasourceId",datasourceId);

        List<Nomatch> resultList = nomatchMapper.getMatchCountByClassList(condition);
        return resultList;
    }

    //删除左侧未匹配同义词
    public int deleteMatch(String nomatchId){
        return nomatchMapper.deleteByPrimaryKey(nomatchId);
    }

    //保存手动匹配结果
    public int saveMatch(String datasourceId, String json){
        int flg = 0;
        //匹配同义词：synonyms表填加数据
        List<String> defineList = JSONArray.parseArray(json, String.class);
        for(String defineDate: defineList){
            JSONObject defineDateJo = JSONObject.fromObject(defineDate);
            String noId = defineDateJo.getString("nomatchId");
            String noName = defineDateJo.getString("nomatchName");
            String codeId = defineDateJo.getString("codeId");
            //删除表：nomatch中手动匹配同义词
            flg = nomatchMapper.deleteByPrimaryKey(noId);
            // 匹配同义词：synonyms表填加数据
            Synonym synonym = new Synonym();
            synonym.setSynId(UUIDUtil.createUUID());
            synonym.setSynName(noName);
            synonym.setCodeId(codeId);
            flg = synonymMapper.insert(synonym);
        }
        return flg;
    }

}
