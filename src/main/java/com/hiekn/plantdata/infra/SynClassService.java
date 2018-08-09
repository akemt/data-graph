package com.hiekn.plantdata.infra;

import com.hiekn.plantdata.Entity.SqlConfig;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SynClassService {
    List<Map<String,Object>> getClassList();

    Map<String,Object> getCodeList(String classId,String searchStr,int page);

    /**
     * 获取导入码表的源数据
     *
     * @param sqlConfig
     * @return
     * @throws SQLException
     */
    Map<String, String> getSourceData(SqlConfig sqlConfig, String codeColumn, String valueColumn) throws SQLException;

}
