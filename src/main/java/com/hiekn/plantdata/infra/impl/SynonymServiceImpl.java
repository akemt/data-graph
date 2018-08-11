package com.hiekn.plantdata.infra.impl;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.plantdata.Entity.*;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.infra.SynonymService;
import com.hiekn.plantdata.mapper.DatasourceMapper;
import com.hiekn.plantdata.mapper.NomatchMapper;
import com.hiekn.plantdata.mapper.SynonymMapper;
import com.hiekn.plantdata.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SynonymServiceImpl implements SynonymService {

    @Autowired
    private SynonymMapper synonymMapper;
    @Autowired
    private DatasourceMapper datasourceMapper;
    @Autowired
    private NomatchMapper nomatchMapper;

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

    @Override
    public List<String> getSourceData(SqlConfig sqlConfig, String valueColumn) throws SQLException {
        List<String> result = new ArrayList<>();

        Connection conn = JdbcUtils.getConn(sqlConfig, "oracle.jdbc.driver.OracleDriver");
        String sql = sqlConfig.getSql();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(valueColumn));
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

    @Override
    @Transactional
    public ImportResult insertSyn(SqlConfig sqlConfig, String dataSourceName, List<String> dataList, String classId) {
        // 保存数据源
        Datasource datasource = new Datasource();
        datasource.setDatasourceId(UUIDUtil.createUUID());
        datasource.setDatasourceName(dataSourceName);
        datasource.setClassId(classId);
        datasource.setConfig(JSONObject.toJSONString(sqlConfig));
        datasourceMapper.insert(datasource);

        // 获取已存在的同义词，比较源数据和数据库中的同义词列表，存在则match数加一，不存在则nomatch数加一并再nomatch表中插入一条数据
        int match = 0, nomatch = 0;
        List<String> synList = synonymMapper.selectSynByClassId(classId);
        for(String sourceSyn : dataList) {
            if (synList.contains(sourceSyn)) {
                match++;
            } else {
                Nomatch oNomatch = new Nomatch();
                oNomatch.setNomatchId(UUIDUtil.createUUID());
                oNomatch.setClassId(classId);
                oNomatch.setDatasourceId(datasource.getDatasourceId());
                oNomatch.setNomatchName(sourceSyn);
                nomatchMapper.insert(oNomatch);
                nomatch++;
            }
        }

        // 返回结果，若有nomatch则status为false，否则为true
        ImportResult importResult = new ImportResult();
        importResult.setDatasourceId(datasource.getDatasourceId());
        importResult.setMatch(match);
        importResult.setNomatch(nomatch);
        importResult.setStatus(nomatch > 0 ? false : true);
        return importResult;
    }

}
