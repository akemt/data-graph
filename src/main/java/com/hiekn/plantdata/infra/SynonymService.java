package com.hiekn.plantdata.infra;

import com.hiekn.plantdata.Entity.Synonym;

import java.util.List;
import java.util.Map;

public interface SynonymService {
    List<Map<String,Object>> getSynListByCodeId(String codeId);

    List<Map<String,Object>> getSynImportedDatasourece(String classId);

    int manualAddSyn(Synonym synonym);

    int manualUpdateSyn(Synonym synonym);

    int deleteSyn(String synId);



}
