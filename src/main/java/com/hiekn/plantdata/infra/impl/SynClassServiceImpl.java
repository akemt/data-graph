package com.hiekn.plantdata.infra.impl;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.plantdata.Entity.*;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.exception.Constant;
import com.hiekn.plantdata.infra.SynClassService;
import com.hiekn.plantdata.mapper.ClassifyMapper;
import com.hiekn.plantdata.mapper.CodeMapper;
import com.hiekn.plantdata.mapper.DatasourceMapper;
import com.hiekn.plantdata.util.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SynClassServiceImpl implements SynClassService {

    @Autowired
    private ClassifyMapper classifyMapper;

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Override
    public List<Classify> getClassList() {
        return classifyMapper.selectAll();
    }

    @Override
    public Map<String, Object> getCodeList(String classId, String searchStr, int page) {
        Map<String, Object> map = new HashMap<>();
        int pageSize = 10;
        //一页显示10条
        map.put("pageSize", pageSize);
        int count = codeMapper.getCodeCountByClass(classId);
        map.put("pageCount", (count - 1) / pageSize + 1);
        map.put("page", page);
        //查询条件
        Map<String, Object> condition = new HashMap<>();
        condition.put("classId", classId);
        condition.put("searchStr", searchStr);
        condition.put("startNum", (page - 1) * pageSize + 1);
        condition.put("endNum", page * pageSize);
        List<Code> resultList = codeMapper.getCodeListByClassId(condition);
        map.put("define", resultList);

        return map;

    }

    @Override
    public Map<String, String> getSourceData(SqlConfig sqlConfig) throws SQLException {
        Map<String, String> result = new HashMap<>();

        Connection conn = JdbcUtils.getConn(sqlConfig, "oracle.jdbc.driver.OracleDriver");
        String sql = sqlConfig.getSql();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                // 暂定规则：如果列码重复则跳过
                if (result.get(rs.getString(sqlConfig.getCodeColumn())) == null) {
                    result.put(rs.getString(sqlConfig.getCodeColumn()), rs.getString(sqlConfig.getValueColumn()));
                }
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
    public ImportResult insertCodes(SqlConfig sqlConfig, Map<String, String> dataMap) {
        // 码表大类表中插入一条数据
        Classify classify = new Classify();
        String classId = UUIDUtil.createUUID();
        classify.setClassId(classId);
        classify.setClassName(sqlConfig.getName());
        classifyMapper.insert(classify);

        // 保存数据源
        Datasource datasource = new Datasource();
        datasource.setDatasourceId(UUIDUtil.createUUID());
        datasource.setDatasourceName(sqlConfig.getName());
        datasource.setClassId(classId);
        datasource.setConfig(JSONObject.toJSONString(sqlConfig));
        datasource.setKind(Constant.DATASOURCE_KIND_CODE);
        datasourceMapper.insert(datasource);

        // 在码表中批量插入数据
        for (String codeColumn : dataMap.keySet()) {
            Code code = new Code();
            code.setCodeId(UUIDUtil.createUUID());
            code.setClassId(classId);
            code.setCode(codeColumn);
            code.setCodeName(dataMap.get(codeColumn));
            codeMapper.insert(code);
        }

        ImportResult importResult = new ImportResult();
        importResult.setDatasourceId(datasource.getDatasourceId());
        importResult.setMatch(dataMap.size());
        importResult.setNomatch(0);
        return importResult;
    }
}
