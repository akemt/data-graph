package com.hiekn.plantdata.infra;

import com.hiekn.plantdata.Entity.Classify;
import com.hiekn.plantdata.Entity.Datasource;
import com.hiekn.plantdata.Entity.ImportResult;
import com.hiekn.plantdata.Entity.SqlConfig;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SynClassService {
    List<Classify> getClassList();

    Map<String, Object> getCodeList(String classId, String searchStr, int page);

    /**
     * 获取导入码表的源数据
     *
     * @param sqlConfig
     * @return
     * @throws SQLException
     */
    Map<String, String> getSourceData(SqlConfig sqlConfig) throws SQLException;

    /**
     * 在码表名称表中插入一条数据，然后批量插入对应的码表
     *
     * @param sqlConfig
     * @param dataMap
     * @return
     */
    ImportResult insertCodes(SqlConfig sqlConfig, Map<String, String> dataMap);

    /**
     * 再次导入码表
     *
     * @param classId 码表大类id
     * @return
     */
    ImportResult insertCodesAgain(String classId) throws SQLException;
}
