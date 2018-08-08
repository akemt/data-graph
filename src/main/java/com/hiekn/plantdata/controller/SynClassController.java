package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.SynClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
     *
     * @return
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public Result getClassList() {
        List<Map<String,Object>> mapList = new ArrayList<>();
        mapList = synClassService.getClassList();
            return Result.success(mapList, 200, "请求成功!");
    }

}
