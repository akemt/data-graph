package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.Entity.Classify;
import com.hiekn.plantdata.Entity.ImportResult;
import com.hiekn.plantdata.Entity.SqlConfig;
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
     * @param name        新码表名称
     * @param codeColumn  新码表id列
     * @param valueColumn 新码表值列
     * @return
     */
    @PostMapping(value = "/import")
    @ResponseBody
    public Result<ImportResult> importCodes(SqlConfig sqlConfig, String name, String codeColumn, String valueColumn) {

        if (name == null || "".equals(name)) {
            return Result.failure(null, "码表名称为空");
        }

        // 码表名称是否重复则直接返回错误信息
        for (Classify classify : synClassService.getClassList()) {
            if (name.equals(classify.getClassName())) {
                return Result.failure(null, "码表名称已存在");
            }
        }

        try {
            // 先获取源数据，再批量插入新数据
            Map<String, String> map = synClassService.getSourceData(sqlConfig, codeColumn, valueColumn);
            synClassService.insertCodes(name, map);
        } catch (SQLException e) {
            return Result.failure(null, "批量导入码表失败。" + e.getMessage());
        }
        return Result.success(null, 200, "导入成功");
    }
}
