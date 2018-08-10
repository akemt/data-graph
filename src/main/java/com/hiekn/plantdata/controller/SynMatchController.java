package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.Entity.Nomatch;
import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.SynMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 同义词大类控制层
 */
@RestController
@RequestMapping(value = "syn/match")
public class SynMatchController {

    @Autowired
    private SynMatchService synMatchService;

    /**
     * 获得左侧列表+搜索
     *
     * @param datasourceId
     * @return
     */
    @GetMapping(value = "/{datasourceId}/list")
    @ResponseBody
    public Result getCodeList(@PathVariable(value = "datasourceId") String datasourceId) {
        List<Nomatch> nomatchList = synMatchService.getMatchCodeList(datasourceId);
        return Result.success(nomatchList, 200, "请求成功!");
    }

    /**
     * 删除左侧未匹配同义词
     *
     * @param nomatchId
     * @return
     */
    @DeleteMapping(value = "/{nomatchId}")
    @ResponseBody
    public Result deleteMatch(@PathVariable(value = "nomatchId") String nomatchId) {
        if (synMatchService.deleteMatch(nomatchId)>0) {
            return Result.success("", 200, "删除成功!");
        }else {
            return Result.success("",400,"删除失败!");
        }
    }

    /**
     * 保存手动匹配结果
     *
     * @param datasourceId
     * @return
     */
    @PostMapping(value = "/{datasourceId}")
    @ResponseBody
    public Result saveMatch(@PathVariable(value = "datasourceId") String datasourceId, @RequestParam(value = "json") String json) {
        if (synMatchService.saveMatch(datasourceId,json)>0) {
            return Result.success("", 200, "保存成功!");
        }else {
            return Result.success("",400,"保存失败!");
        }
    }

}
