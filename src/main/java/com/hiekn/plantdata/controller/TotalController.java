package com.hiekn.plantdata.controller;


import com.google.gson.reflect.TypeToken;
import com.hiekn.plantdata.bean.graph.EntityBean;
import com.hiekn.plantdata.bean.graph.GraphBean;
import com.hiekn.plantdata.bean.graph.SchemaBean;
import com.hiekn.plantdata.bean.rest.RestResp;
import com.hiekn.plantdata.common.Result;
import com.hiekn.plantdata.infra.GraphService;
import com.hiekn.plantdata.util.JSONUtils;
import com.hiekn.plantdata.util.LoaderJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "total")
public class TotalController {


    @Autowired
    private GraphService graphService;

    /**
     * 获取所有neo4j Key 值
     *
     * @param session
     * @return
     */
    @GetMapping(value = "/neo4jKey")
    public Result batchImportEntityInfo(HttpSession session) {
        List<String> stringList = LoaderJsonUtils.getJsonKey();

        return Result.success(stringList, 200, "保存成功!");
    }

    @PostMapping(value = "/graph")
    public RestResp<GraphBean> getNeo4jGraph(@FormParam("pageSize")Integer pageSize,@RequestParam(value = "key") String key,HttpSession session,@FormParam("id") Long id, @FormParam("distance") @DefaultValue("1") Integer distance,@FormParam("isRelationMerge") @DefaultValue("false") Boolean isRelationMerge, @FormParam("allowAtts") String allowAtts, @FormParam("allowTypes") String allowTypes,  @QueryParam("token") String token, @QueryParam("tt") Long tt) {
        String usrId = (String) session.getAttribute("userId");

        List<String> allowAttList = (List) JSONUtils.fromJson(allowAtts, (new TypeToken<List<String>>() {
        }).getType());
        List<String> allowTypeList = (List) JSONUtils.fromJson(allowTypes, (new TypeToken<List<String>>() {
        }).getType());

        GraphBean graphBean = this.graphService.getNeo4jGraph(usrId, key, id,distance,allowAttList, allowTypeList, isRelationMerge.booleanValue(),pageSize);
        RestResp<GraphBean> rs = null;
        if(graphBean != null){
            rs = new RestResp(graphBean, 0l);
        } else{
            rs = new RestResp("NEO4Jjson配置文件中，key:"+key+",没有对应的CQL语句", 0l);
        }
        return rs;
    }

    /**
     * 实体的下拉提示
     *
     * @param kgName
     * @param kw
     * @param pageSize
     * @param allowTypes
     * @param isCaseSensitive
     * @param token
     * @param tt
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/prompt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RestResp<EntityBean> prompt(HttpSession session, @FormParam("kgName") String kgName, @FormParam("kw") String kw, @FormParam("pageSize")Integer pageSize, @FormParam("allowTypes") String allowTypes, @FormParam("isCaseSensitive") @DefaultValue("false") Boolean isCaseSensitive, @QueryParam("token") String token, @QueryParam("tt") Long tt) {
        List<Long> allowTypesList = (List) JSONUtils.fromJson(allowTypes, (new TypeToken<List<Long>>() {}).getType());
        String userId = (String) session.getAttribute("userId");
        List<EntityBean> rsList = this.graphService.getPrompt(userId, kw, allowTypesList);

        RestResp<EntityBean> rs = new RestResp(rsList, tt);
        return rs;
    }

    @GetMapping(value = "/nodeID/{key}")
    @ResponseBody
    public Result getTotalNodeID(@PathVariable(value = "key") String key, HttpSession session) {

        String userId = (String) session.getAttribute("userId");
//        String userId = "2";
        List<Long> mapList = graphService.getGraphTotalNodeByKey(userId,key);
        Map<String, Object> map = new HashMap<>();
        if(mapList.size()>0){
            map.put("nodeID",mapList.get(0));
        }else{
            map.put("nodeID","");
        }
        return Result.success(map, 200, "请求成功!");
    }
}
