package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.Entity.SqlConfig;
import com.hiekn.plantdata.exception.Constant;
import com.hiekn.plantdata.infra.SynOperateService;
import com.hiekn.plantdata.mapper.CodeMapper;
import com.hiekn.plantdata.mapper.NomatchMapper;
import com.hiekn.plantdata.mapper.SynonymMapper;
import com.hiekn.plantdata.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SynOperateServiceImpl implements SynOperateService {

    @Autowired
    private NomatchMapper nomatchMapper;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private SynonymMapper synonymMapper;

    @Override
    public List<Map<String,Object>> getNomatchList(){
        return nomatchMapper.getNomatchList();
    }

    @Override
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

    @Override
    public List<Map<String,Object>> getSearchCount(String searchStr){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        //内码数
        int codeCount = codeMapper.getCodeCountByName(searchStr);
        map1.put("name","内码");
        map1.put("count",codeCount);
        list.add(map1);

        Map<String,Object> map2 = new HashMap<>();
        //同义词数
        int synCount = synonymMapper.getSynCountByName(searchStr);
        map2.put("name","同义词");
        map2.put("count",synCount);
        list.add(map2);

        return list;

    }

    @Override
    public List<Map<String,Object>> getSearchResult(String searchStr,String type){
        List<Map<String,Object>> list = new ArrayList<>();
        if ("内码".equals(type)){
            list = synonymMapper.searchCodeNameInfo(searchStr);
        }else if ("同义词".equals(type)){
            list = synonymMapper.searchSynNameInfo(searchStr);
        }
        return list;
    }

    @Override
    public List<String> getColumns(SqlConfig sqlConfig) throws SQLException {
        List<String> result = new ArrayList<>();

        Connection conn = JdbcUtils.getConn(sqlConfig, Constant.DB_DRIVER);
        String sql = sqlConfig.getSql();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                result.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            rs.close();
            pstmt.close();
            conn.close();
        }
        return result;
    }

}
