package com.hiekn.plantdata.infra;

import com.hiekn.plantdata.Entity.Nomatch;

import java.util.List;

public interface SynMatchService {

    //获得左侧列表+搜索
    List<Nomatch> getMatchCodeList(String datasourceId);

    //删除左侧未匹配同义词
    int deleteMatch(String nomatchId);

    //保存手动匹配结果
    int saveMatch(String datasourceId, String json);

}
