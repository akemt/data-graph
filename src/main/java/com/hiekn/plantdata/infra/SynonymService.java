package com.hiekn.plantdata.infra;

import com.hiekn.plantdata.Entity.ImportResult;
import com.hiekn.plantdata.Entity.SqlConfig;
import com.hiekn.plantdata.Entity.Synonym;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SynonymService {
    List<Map<String,Object>> getSynListByCodeId(String codeId);

    List<Map<String,Object>> getSynImportedDatasourece(String classId);

    int manualAddSyn(Synonym synonym);

    int manualUpdateSyn(Synonym synonym);

    int deleteSyn(String synId);

    /**
     * 获取导入同义词表的源数据
     *
     * @param sqlConfig
     * @return
     * @throws SQLException
     */
    Set<String> getSourceData(SqlConfig sqlConfig) throws SQLException;

    /**
     * 导入同义词
     *
     * @param sqlConfig
     * @param dataSet
     * @param classId
     * @return
     */
    ImportResult insertSyn(SqlConfig sqlConfig, Set<String> dataSet, String classId);

    /**
     * 再次导入同义词
     *
     * @param datasourceId
     * @return
     */
    ImportResult insertSynAgain(String datasourceId) throws SQLException;
}
