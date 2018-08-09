package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.SynOperateService;
import com.hiekn.plantdata.infra.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
