package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.Entity.SqlConfig;
import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.SynOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同义词操作控制层
 */
@RestController
@RequestMapping(value = "syn")
public class SynOperateController {

    @Autowired
    private SynOperateService synOperateService;

    /**
     * 带解决问题，左侧大类未匹配数目列表
     *
     * @return
     */
    @GetMapping(value = "/nomatch/list")
    @ResponseBody
    public Result getNomatchList() {

        List<Map<String,Object>> list = new ArrayList<>();
        list = synOperateService.getNomatchList();
        return Result.success(list, 200, "请求成功!");
    }


    /**
     * 带解决问题，右侧获取未匹配大类的详细信息
     * @Param classId
     * @param page
     * @return
     */
    @GetMapping(value = "/nomatch/list/{classId}")
    @ResponseBody
    public Result getNomatchInfo(@PathVariable(value = "classId") String classId,@RequestParam(value = "page") int page) {

        Map<String,Object> map = new HashMap<>();
        map = synOperateService.getNomatchInfo(classId,page);
        return Result.success(map, 200, "请求成功!");
    }

    /**
     * 搜索页 返回搜索结果数量
     * @param searchStr
     * @return
     */
    @GetMapping(value = "/search/count")
    @ResponseBody
    public Result getSearchCount(@RequestParam(value = "searchStr",required = false) String searchStr) {

        List<Map<String,Object>> list = new ArrayList<>();
        list = synOperateService.getSearchCount(searchStr);
        return Result.success(list, 200, "请求成功!");
    }

    /**
     * 搜索页 返回搜索结果数量
     * @param type
     * @param searchStr
     * @return
     */
    @GetMapping(value = "/search")
    @ResponseBody
    public Result getSearchResult(@RequestParam(value = "searchStr",required = false) String searchStr,@RequestParam(value = "type") String type) {

        List<Map<String,Object>> list = new ArrayList<>();
        list = synOperateService.getSearchResult(searchStr,type);
        return Result.success(list, 200, "请求成功!");
    }

    /**
     * 读取字段
     *
     * @param sqlConfig
     * @return
     */
    @PostMapping(value = "/getColumns")
    @ResponseBody
    public Result<List<String>> getColumns(SqlConfig sqlConfig) {

        List<String> list = new ArrayList<>();
        try {
            list = synOperateService.getColumns(sqlConfig);
            return Result.success(list, 200, "请求成功");
        } catch (SQLException e) {
            return Result.failure(null);
        }
    }
}
