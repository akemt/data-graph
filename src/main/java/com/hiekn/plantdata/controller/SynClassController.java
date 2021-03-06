package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.Entity.Classify;
import com.hiekn.plantdata.Entity.Datasource;
import com.hiekn.plantdata.Entity.ImportResult;
import com.hiekn.plantdata.Entity.SqlConfig;
import com.hiekn.plantdata.common.Assert;
import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.infra.SynClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同义词大类控制层
 */
@RestController
@RequestMapping(value = "syn/class")
public class SynClassController {

    @Autowired
    private SynClassService synClassService;

    /**
     * 获得大类列表
     * @return
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public Result getClassList() {
        List<Classify> mapList = new ArrayList<>();
        mapList = synClassService.getClassList();
        return Result.success(mapList, 200, "请求成功!");
    }

    /**
     * 获得码表列表
     *
     * @param searchStr
     * @param classId
     * @return
     */
    @GetMapping(value = "/{classId}/codeList")
    @ResponseBody
    public Result getCodeList(@PathVariable(value = "classId") String classId,@RequestParam(value = "searchStr",required = false) String searchStr,@RequestParam(value = "page") int page) {
        Map<String,Object> map = new HashMap<>();
        map = synClassService.getCodeList(classId,searchStr,page);
        return Result.success(map, 200, "请求成功!");
    }

    /**
     * 导入新码表
     *
     * @param sqlConfig
     * @return
     */
    @PostMapping(value = "/import")
    @ResponseBody
    public Result<ImportResult> importCodes(SqlConfig sqlConfig) {

        if (Assert.isEmpty(sqlConfig.getName())) {
            return Result.failure(null, "码表名称为空");
        }

        // 码表名称是否重复则直接返回错误信息
        for (Classify classify : synClassService.getClassList()) {
            if (sqlConfig.getName().equals(classify.getClassName())) {
                return Result.failure(null, "码表名称已存在");
            }
        }

        try {
            // 先获取源数据，再批量插入新数据
            Map<String, String> sourceData = synClassService.getSourceData(sqlConfig);
            ImportResult importResult = synClassService.insertCodes(sqlConfig, sourceData);
            return Result.success(importResult, 200, "导入成功");
        } catch (SQLException e) {
            return Result.failure(null, "批量导入码表失败。" + e.getMessage());
        }
    }

    /**
     * 再次导入码表
     *
     * @param classId
     * @return
     */
    @PostMapping(value = "/import/{classId}")
    @ResponseBody
    public Result<ImportResult> importCodes(@PathVariable(value = "classId") String classId) {
        try {
            ImportResult importResult = synClassService.insertCodesAgain(classId);
            return Result.success(importResult, 200, "导入成功");
        } catch (SQLException e) {
            return Result.failure(null, "再次导入码表失败。" + e.getMessage());
        }
    }
}
