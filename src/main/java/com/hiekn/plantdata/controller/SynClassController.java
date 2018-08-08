package com.hiekn.plantdata.controller;

import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.SynClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
        List<Map<String,Object>> mapList = new ArrayList<>();
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

}
