package com.hiekn.plantdata.infra.impl;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.plantdata.Entity.*;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.exception.Constant;
import com.hiekn.plantdata.infra.SynMatchService;
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
import java.util.*;

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
    public Set<String> getSourceData(SqlConfig sqlConfig) throws SQLException {
        Set<String> result = new HashSet<>();

        Connection conn = JdbcUtils.getConn(sqlConfig, "oracle.jdbc.driver.OracleDriver");
        String sql = sqlConfig.getSql();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(sqlConfig.getValueColumn()));
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
    public ImportResult insertSyn(SqlConfig sqlConfig, Set<String> dataSet, String classId) {
        // 保存数据源
        Datasource datasource = new Datasource();
        datasource.setDatasourceId(UUIDUtil.createUUID());
        datasource.setDatasourceName(sqlConfig.getName());
        datasource.setClassId(classId);
        datasource.setConfig(JSONObject.toJSONString(sqlConfig));
        datasource.setKind(Constant.DATASOURCE_KIND_SYN);
        datasourceMapper.insert(datasource);

        ImportResult importResult = matchSyn(dataSet, classId, datasource.getDatasourceId(), null);
        return importResult;
    }

    @Override
    @Transactional
    public ImportResult insertSynAgain(String datasourceId) throws SQLException {
        // 获取数据源并通过jdbc查询数据源数据
        Datasource datasource = datasourceMapper.selectByPrimaryKey(datasourceId);
        SqlConfig sqlConfig = JSONObject.parseObject(datasource.getConfig(), SqlConfig.class);
        Set<String> dataSet = getSourceData(sqlConfig);

        // 获取nomatch表数据
        Map<String,Object> condition = new HashMap<>();
        condition.put("datasourceId",datasourceId);
        List<Nomatch> nomatchList = nomatchMapper.getMatchCountByClassList(condition);

        ImportResult importResult = matchSyn(dataSet, datasource.getClassId(), datasourceId, nomatchList);
        return importResult;
    }

    /**
     * 获取已存在的同义词，比较源数据和数据库中的同义词列表，存在则match数加一，不存在则nomatch数加一并再nomatch表中插入一条数据
     * 插入nomatch表数据时要与原nomatch表数据进行比对，保证同一数据源nomatch数据不重复
     * 返回结果，若有nomatch则status为false，否则为true
     *
     * @param dataSet      源数据
     * @param classId      码表id
     * @param datasourceId 数据源id
     * @param nomatchList  nomatch表中数据
     * @return
     */
    private ImportResult matchSyn(Set<String> dataSet, String classId, String datasourceId, List<Nomatch> nomatchList) {
        int match = 0, nomatch = 0;
        List<String> synList = synonymMapper.selectSynByClassId(classId);

        // 简化nomatch表数据
        Set<String> nomatchSet = new HashSet<>();
        if (nomatchList != null) {
            for (Nomatch tNomatch : nomatchList) {
                nomatchSet.add(tNomatch.getNomatchName());
            }
        }

        for (String sourceSyn : dataSet) {
            if (synList.contains(sourceSyn)) {
                match++;
            } else {
                // 插入nomatch表数据时与原nomatch表数据进行比对，只插入不重复数据，但是都算nomatch数量
                if (!nomatchSet.contains(sourceSyn)) {
                    Nomatch oNomatch = new Nomatch();
                    oNomatch.setNomatchId(UUIDUtil.createUUID());
                    oNomatch.setClassId(classId);
                    oNomatch.setDatasourceId(datasourceId);
                    oNomatch.setNomatchName(sourceSyn);
                    nomatchMapper.insert(oNomatch);
                }
                nomatch++;
            }
        }
        ImportResult importResult = new ImportResult();
        importResult.setDatasourceId(datasourceId);
        importResult.setMatch(match);
        importResult.setNomatch(nomatch);
        importResult.setStatus(nomatch > 0 ? false : true);
        return importResult;
    }
}
