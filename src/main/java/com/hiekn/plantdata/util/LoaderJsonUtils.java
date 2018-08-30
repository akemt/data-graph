package com.hiekn.plantdata.util;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 解析配置JSON文件neo4j_cql
 */
@Slf4j
@Component
public class LoaderJsonUtils {

    private static Hashtable<String, String> table = new Hashtable<String, String>();

    private static List<String> mapList = new ArrayList<>();

    static{
        LoaderJsonUtils loaderJsonUtils = new LoaderJsonUtils();
        JSONArray jsonArray = loaderJsonUtils.getJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String strkey = jsonObject.getString("cql_key");
            String strValue = jsonObject.getString("cql_value");
            table.put(strkey,strValue);
            mapList.add(strkey);
        }
        log.info("Hashtable getJsonValue:" + table);
    }

    /**
     * 根据neo4jJson 文件key值查找value
     * @param key
     * @return
     */
    public static String getNeo4jJsonValueByKey(String key) {
        String value = "";
        if (table.containsKey(key)) {
            value = table.get(key);
        }
        log.info("getNeo4jJsonValueByKey:" + value);
        return value;
    }

    /**
     * 加载json 文件
     * @return
     */
    public JSONArray getJsonArray() {
        String path = getClass().getClassLoader().getResource("neo4j_cql.json").toString();
        path = path.replace("\\", "/");
        if (path.contains(":")) {
            //path = path.substring(6);// 1
            path = path.replace("file:/", "");// 2
        }
        JSONArray jsonArray = null;
        try {
            String input = FileUtils.readFileToString(new File(path), "UTF-8");
            jsonArray = JSONObject.parseArray(input);
        } catch (Exception e) {
            e.printStackTrace();
            jsonArray = null;
        }
        return jsonArray;
    }

    /**
     * 获取neo4j KEY值列表
     *
     * @return
     */
    public static List<String> getJsonKey() {
        log.info("getJsonKey:" + mapList);
        return mapList;
    }
}
