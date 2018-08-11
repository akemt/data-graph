package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.Entity.*;
import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.infra.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.*;

/**
 * 同义词控制层
 */
@RestController
@RequestMapping(value = "syn/synonyms")
public class SynonymController {

    @Autowired
    private SynonymService synonymService;

    /**
     * 获得一个词的所有同义词列表
     *
     * @param codeId
     * @return
     */
    @GetMapping(value = "/{codeId}/list")
    @ResponseBody
    public Result getSynListByCodeId(@PathVariable(value = "codeId") String codeId) {

        List<Map<String,Object>> list = new ArrayList<>();
        list = synonymService.getSynListByCodeId(codeId);
        return Result.success(list, 200, "请求成功!");
    }

    /**
     * 获得曾经导入同义词表数据源
     *
     * @param classId
     * @return
     */
    @GetMapping(value = "/importInfo/{classId}")
    @ResponseBody
    public Result getSynImportedDatasourece(@PathVariable(value = "classId") String classId) {

        List<Map<String,Object>> list = new ArrayList<>();
        list = synonymService.getSynImportedDatasourece(classId);
        return Result.success(list, 200, "请求成功!");
    }

    /**
     * 手动新增同义词
     *
     * @param codeId
     * @param value
     * @return
     */
    @PostMapping(value = "/{codeId}/addSyn")
    @ResponseBody
    public Result manualAddSyn(@PathVariable(value = "codeId") String codeId,@RequestParam("value") String value) {
        String uuid = UUIDUtil.createUUID();
        Synonym synonym = new Synonym();
        synonym.setSynId(uuid);
        synonym.setSynName(value);
        synonym.setCodeId(codeId);
        if (synonymService.manualAddSyn(synonym)>0) {
            return Result.success(uuid, 200, "新增成功!");
        }else {
            return Result.success("",200,"新增失败!");
        }
    }

    /**
     * 手动修改同义词
     *
     * @param synId
     * @param value
     * @return
     */
    @PostMapping(value = "/{synId}")
    @ResponseBody
    public Result manualUpdateSyn(@PathVariable(value = "synId") String synId,@RequestParam("value") String value) {
        Synonym synonym = new Synonym();
        synonym.setSynId(synId);
        synonym.setSynName(value);
        if (synonymService.manualUpdateSyn(synonym)>0) {
            return Result.success("", 200, "修改成功!");
        }else {
            return Result.success("",200,"修改失败!");
        }
    }

    /**
     * 删除同义词
     *
     * @param synId
     * @return
     */
    @DeleteMapping(value = "/{synId}")
    @ResponseBody
    public Result manualDeleteSyn(@PathVariable(value = "synId") String synId) {
        if (synonymService.deleteSyn(synId)>0) {
            return Result.success("", 200, "删除成功!");
        }else {
            return Result.success("",200,"删除失败!");
        }
    }

    /**
     * 导入新同义词
     *
     * @param sqlConfig
     * @param name        数据源名称
     * @param valueColumn 同义词值列
     * @param classId     码表id
     * @return
     */
    @PostMapping(value = "/import")
    @ResponseBody
    public Result<ImportResult> importSyn(SqlConfig sqlConfig, String name, String valueColumn, String classId) {

        if (name == null || "".equals(name)) {
            return Result.failure(null, "数据源名称为空");
        }

        try {
            // 先获取源数据，再批量插入新数据
            List<String> sourceData = synonymService.getSourceData(sqlConfig, valueColumn);
            ImportResult importResult = synonymService.insertSyn(sqlConfig, name, sourceData, classId);
            return Result.success(importResult, 200, "导入成功");
        } catch (Exception e) {
            return Result.failure(null, "批量导入同义词失败。" + e.getMessage());
        }
    }
}
