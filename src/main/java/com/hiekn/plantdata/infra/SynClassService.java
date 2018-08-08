package com.hiekn.plantdata.infra;

import java.util.List;
import java.util.Map;

public interface SynClassService {
    List<Map<String,Object>> getClassList();

    Map<String,Object> getCodeList(String classId,String searchStr,int page);

}
