package com.hiekn.plantdata.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import static javax.xml.transform.OutputKeys.ENCODING;

/**
 * 解析配置JSON文件neo4j_cql
 */
@Slf4j
@Component
public class LoaderJsonUtils {

//    @Value("classpath:neo4j_cql.json")
//    private Resource areaRes;

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


    private JSONArray readWordFile() throws Exception{
        JSONArray jsonArray = null;
////		File file = ResourceUtils.getFile("classpath:key.txt");
////		System.out.println(file.toString());
////		String url = SensitiveWordInit.class.getResource("/").getFile();
////		System.out.println("readSensitiveWordFile" + url);
////		File file = new File(url+"key.txt");    //读取文件
//        InputStream inputStream = SensitiveWordInit.class.getResourceAsStream("/key.txt");
//        InputStreamReader read = new InputStreamReader(inputStream,ENCODING);
//        try {
//            BufferedReader bufferedReader = new BufferedReader(read);
//            String txt ;
//            StringBuffer areaData = new StringBuffer();
//            while((txt = bufferedReader.readLine()) != null){    //读取文件，将文件内容放入到set中
//                areaData.append(txt);
//            }
//            jsonArray = JSONObject.parseArray(areaData.toString());
//        } catch (Exception e) {
//            throw e;
//        }finally{
//            read.close();     //关闭文件流
//        }
        return jsonArray;
    }


    /**
     * 加载json 文件
     * @return
     */
    public JSONArray getJsonArray() {
        JSONArray jsonArray = null;
//        try {
//            String areaData =  IOUtils.toString(areaRes.getInputStream(), Charset.forName("UTF-8"));
//            jsonArray = JSONObject.parseArray(areaData);
//        } catch (IOException e) {
//            e.printStackTrace();
//            jsonArray = null;
//        }

//        String path = getClass().getClassLoader().getResource("neo4j_cql.json").toString();
//        path = path.replace("\\", "/");
//        if (path.contains(":")) {
//            //path = path.substring(6);// 1
//            path = path.replace("file:/", "");// 2
//        }
//        JSONArray jsonArray = null;
//        try {
//            String input = FileUtils.readFileToString(new File(path), "UTF-8");
//            jsonArray = JSONObject.parseArray(input);
//        } catch (Exception e) {
//            e.printStackTrace();
//            jsonArray = null;
//        }

        InputStream stream = getClass().getClassLoader().getResourceAsStream("neo4j_cql.json") ;

//        log.info("infile:"+infile);
        StringBuffer sb = new StringBuffer() ;
        BufferedReader br = null ;
        try {
            br = new BufferedReader(new InputStreamReader(stream,"UTF-8")) ;
            String s=null ;
            while((s=br.readLine()) !=null){
                sb.append(s) ;
            }
            br.close();
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException:"+e);
        } catch (IOException e) {
            log.error("IOException:"+e);
        }finally {
            if(br !=null){
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("close br error:"+e);
                }
            }
        }
        jsonArray = JSONObject.parseArray(sb.toString());
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
