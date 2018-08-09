package com.hiekn.plantdata.infra;

import java.util.List;
import java.util.Map;

public interface SynOperateService {
    List<Map<String,Object>> getNomatchList();

    Map<String,Object> getNomatchInfo(String classId,int page);

}
