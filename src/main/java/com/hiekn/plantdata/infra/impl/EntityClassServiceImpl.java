package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.Entity.*;
import com.hiekn.plantdata.common.AttrTreeNode;
import com.hiekn.plantdata.common.TreeBuilder;
import com.hiekn.plantdata.common.TreeNode;
import com.hiekn.plantdata.common.UUIDUtil;
import com.hiekn.plantdata.dao.EntitysRepository;
import com.hiekn.plantdata.infra.EntityClassService;
import com.hiekn.plantdata.infra.Neo4jDriverService;
import com.hiekn.plantdata.mapper.EntityMapper;
import com.hiekn.plantdata.mapper.ModelMapper;
import com.hiekn.plantdata.util.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.neo4j.ogm.response.model.NodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class EntityClassServiceImpl implements EntityClassService {

    @Autowired
    private Neo4jDriverService neo4jDriverService;

    @Autowired
    private EntitysRepository entitysRepository;
    @Autowired
    private EntityClassService entityClassService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public Map<String, Object> saveEntitysClassInfo(String userId, String eClassname) {

        //验证当前用户下，是否存在该名称
        List<Map<String, String>> mapList = modelMapper.getUsrGraphEntityTypeListByName(userId, eClassname);


        Map<String, Object> map = new HashMap<>();
        if (mapList != null && mapList.size() > 0) {//该名称已经存在，不可以新建
            map.put("id", null);
        } else {
            long ids = 0l;
            //查看实体类label名是否存在
            List<ResultheadData> resultheadDataList = entitysRepository.findEntitysClassList(eClassname);
            if (resultheadDataList != null && resultheadDataList.size() > 0) {//当前neo4j 数据库中，该名称已经存在，不可以新建
                List<Map<String, Object>> pNodeinfoList = this.getResultheadDataList(resultheadDataList);
                if (pNodeinfoList != null && pNodeinfoList.size() > 0) {
                    ids = (long) pNodeinfoList.get(0).get("id");
                }

            } else {
                //保存实体类
                String jGlobalName = "实体类#" + eClassname;
                String strAttr = "MERGE (a:实体类:level2 {global_name:'" + jGlobalName + "',name: '" + eClassname + "'})  RETURN id(a)  as id";
                ids = neo4jDriverService.saveLabelsReturnID(strAttr);
            }

            map.put("id", ids);
            //保存实体类到关系数据库
            EntityType modelType = new EntityType();
            modelType.setID(UUIDUtil.createUUID());
            modelType.setEntSID(String.valueOf(ids));
            modelType.setEntName(eClassname);
            modelType.setUsrSID(userId);
            modelType.setEntTmpl("");
            modelMapper.saveEntitysClassInfo(modelType);
        }
        return map;
    }

    @Override
    public Map<String, Object> getEntitysClassInfo(long id, String usrID) {

        List<ResultheadData> infoList = entitysRepository.findById(id);

        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = this.getResultheadDataList(infoList);
        String Labels = "";
        if (list != null && list.size() > 0) {
            map.put("id", list.get(0).get("id"));
            Labels = (String) list.get(0).get("name");
            map.put("name", Labels);
        }

        List<ResultheadData> parentNodeinfoList = entitysRepository.findParentNodeById(id);
        List<Map<String, Object>> pNodeinfoList = this.getResultheadDataList(parentNodeinfoList);
        if (pNodeinfoList != null && pNodeinfoList.size() > 0) {
            map.put("mid", pNodeinfoList.get(0).get("id"));
            map.put("model", pNodeinfoList.get(0).get("name"));
        } else {
            map.put("mid", 00000);
            map.put("model", "无实体类");
        }

        List<List<TreeNode>> defineList = new ArrayList<>();

        //根据当前用户，查询起下的属性节点
        List<Map<String, String>> mapList = modelMapper.getUsrGraphEntityAttrList(usrID, String.valueOf(id));
        List<Long> longList = ArrayUtils.getMapListToListLong(mapList);

        List<ResultheadData> treeList = entitysRepository.findTreeDataById(id, ArrayUtils.listDataToLong(longList));
        Map<String, Object> childrenMap = new HashMap<>();
        List<TreeNode> listNode = new ArrayList<TreeNode>();
        Integer newParentId = 9999999;
        for (int i = 0; i < treeList.size(); i++) {
            ResultheadData resultheadData1 = treeList.get(i);
            for (NodeModel nodesEntity : resultheadData1.getNodes()) {
                if (nodesEntity.getPropertyList().size() > 0) {
                    Object name = nodesEntity.property("name");
//                    Object value = nodesEntity.property("value");
                    List<String> listValue = new ArrayList<>();
//                    if(value !=null){
//                        String str[] = value.toString().split(",");
//                        for(String st:str){
//                            listValue.add(st);
//                        }
//
//                    }
//                    String strValue[]  = ArrayUtils.listDataToString(listValue);
                    Long ids = nodesEntity.getId();

                    Integer parentId = (Integer) nodesEntity.property("parentId");
                    if (parentId == null || id == ids) {
                        parentId = 9999999;
                        newParentId = 9999999;
                    }
                    TreeNode treeNode = new TreeNode(ids, (String) name, parentId.longValue(), listValue);
                    listNode.add(treeNode);
                }
            }
        }
        List<TreeNode> trees_ = TreeBuilder.buildByRecursive(ArrayUtils.removeDuplicateTreeNode(listNode), newParentId);

        if (trees_ != null && trees_.size() > 0) {
            TreeNode treeNode = trees_.get(0);
            map.put("id", treeNode.getId());
            map.put("name", treeNode.getName());
            map.put("define", treeNode.getChildren());
        } else {
            map.put("define", defineList);
        }
        return map;
    }

    /**
     * 公共方法-遍历ResultheadData数据-节点
     *
     * @param infoList
     * @return
     */
    public List<Map<String, Object>> getResultheadDataList(List<ResultheadData> infoList) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < infoList.size(); i++) {

            ResultheadData resultheadData1 = infoList.get(i);
            for (NodeModel nodesEntity : resultheadData1.getNodes()) {
                if (nodesEntity.getPropertyList().size() > 0) {
                    map = new HashMap<>();
                    Object name = nodesEntity.property("name");
                    map.put("name", (String) name);
                    Long ids = nodesEntity.getId();
                    map.put("id", ids);
                    list.add(map);
                }
            }
        }
        return list;
    }

    @Override
    public boolean updateEntityClassInfo(String userId, long id, String json) {
        JSONObject jsonObject = JSONObject.fromObject(json);

        String strId = "";
        if (jsonObject.get("id") != null) {
            strId = jsonObject.getString("id");
        } else {
            strId = String.valueOf(id);
        }
        String name = jsonObject.getString("name");

        //获取当前用户下，此实体类下的所有属性
        List<Map<String, String>> mapListAttr = modelMapper.getUsrGraphEntityAttrList(userId, String.valueOf(id));
        List<Long> longListAttr = ArrayUtils.getMapListToListLong(mapListAttr);
        String longsAttr = ArrayUtils.listDataToStr(longListAttr);
        //获取当前用户下，此实体类下的所有关系
        List<Map<String, String>> mapListRel = modelMapper.getUsrGraphEntityRelList(userId, String.valueOf(id));
        List<Long> longListRel = ArrayUtils.getMapListToListLong(mapListRel);
        String longsRel = ArrayUtils.listDataToStr(longListRel);
        //关系型数据库-删除该实体类 以下的节点和关系
        modelMapper.deleteEntClassAttrInfoByEntID(userId, strId);

        modelMapper.deleteEntClassRelInfoByEntID(userId, strId);

        //NEO4J 删除该实体类 该用户下的节点和关系
        String strdeleteNode = "MATCH (m)-[r]->(n) where id(m) = " + strId + " and id(n) in [" + longsAttr + "]   and id(r) in [" + longsRel + "]  DETACH DELETE r,n;";
        neo4jDriverService.saveLabels(strdeleteNode);

//        entitysRepository.deleteEntitysTypeRelation(Long.valueOf(strId),longsAttr,longsRel);

        String jaStrDefine = jsonObject.getString("define");
        //将jsonArray字符串转化为JSONArray
        JSONArray jsonArrayDefine = JSONArray.fromObject(jaStrDefine);
        String relationName = "实体类_包含_属性";
        this.treeMenuList(userId, jsonArrayDefine, Long.valueOf(strId), Long.valueOf(strId), name, relationName);
        return true;
    }

    @Override
    public List<Map<String, Object>> getEntitysClassList(String userId, String searchStr, Integer iPage) {

        List<Long> longList = entityClassService.getUsrGraphEntityTypeList(userId);
        String kws = "." + "*" + searchStr + "." + "*";
        List<ResultheadData> infoList = entitysRepository.findEntitysClassList(ArrayUtils.listDataToLong(longList), kws, iPage);
        List<Map<String, Object>> list = this.getResultheadDataList(infoList);
        return list;
    }

    @Override
    public List<Map<String, Object>> getAttributesList(String mid, String searchStr, Integer iPage) {
        String kws = "." + "*" + searchStr + "." + "*";
        List<Map<String, Object>> list = new ArrayList<>();
        if (!mid.equals("null")) {// mid 不为Null ,则查询此实体其下的属性
            List<LinkedHashMap> infoList = entitysRepository.findEntitysAttrList(Long.valueOf(mid), kws, iPage);
            Map<String, Object> map = null;
            for (int i = 0; i < infoList.size(); i++) {
                LinkedHashMap nodeList = infoList.get(i);
                NodeModel nodeModels = (NodeModel) nodeList.get("nodes");
                if (nodeModels.getPropertyList().size() > 0) {
                    map = new HashMap<>();
                    Object name = nodeModels.property("name");
                    map.put("name", (String) name);
                    Long ids = nodeModels.getId();
                    map.put("id", ids);
                    list.add(map);
                }
            }

        } else {// mid 为Null ,则查询所有实体类其下的属性
            List<ResultheadData> infoList = entitysRepository.findEntitysAttrList(kws, iPage);
            list = this.getResultheadDataList(infoList);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getRelbutesList(String mid, String searchStr, Integer iPage) {
        String kws = "." + "*" + searchStr + "." + "*";
        List<Map<String, Object>> list = new ArrayList<>();
        if (!"".equals(mid)) {// mid 不为Null ,则查询此实体其下的属性
            List<LinkedHashMap> infoList = entitysRepository.findEntitysRelationList(Long.valueOf(mid), kws, iPage);
            Map<String, Object> map = null;
            for (int i = 0; i < infoList.size(); i++) {
                LinkedHashMap nodeList = infoList.get(i);
                NodeModel nodeModels = (NodeModel) nodeList.get("relationType");
                if (nodeModels.getPropertyList().size() > 0) {
                    map = new HashMap<>();
                    Object name = nodeModels.property("name");
                    map.put("name", (String) name);
                    Long ids = nodeModels.getId();
                    map.put("id", ids);
                    list.add(map);
                }
            }

        }
        return list;
    }


    /**
     * 递归----获取某个父节点下面的所有子节点
     *
     * @param jsonArrayDefine
     */
    public void treeMenuList(String userId, JSONArray jsonArrayDefine, long nodeID, long entID, String strRelationName, String relationName) {
        if (jsonArrayDefine.size() > 0) {
            for (int i = 0; i < jsonArrayDefine.size(); i++) {

                String jAttrName = jsonArrayDefine.getJSONObject(i).getString("name");

//                String jAttrValue = jsonArrayDefine.getJSONObject(i).getString("value");
//
//                System.out.println("Name: " + jAttrName + ",Value: " + JSONArray.fromObject(jAttrValue).toString());
//                JSONArray jValue = JSONArray.fromObject(jAttrValue);
//                String value = "";
//                for (int k =0;k<jValue.size();k++) {
//                    value += jValue.getString(k)+",";
//                }`"+strLabelsName+"`
                String strGlobalName = userId + ":实体类#" + strRelationName + "$" + jAttrName;
                //实体类##属性@@XX
//                String strLabelsName = "实体类##属性##"+strRelationName+"@@"+jAttrName;
                //建立节点
                String strAttrLabels = "MERGE (a:属性:level2  {global_name:'" + strGlobalName + "',name: '" + jAttrName + "',parentId: " + nodeID + "})   RETURN id(a)  as id";

                long ids = neo4jDriverService.saveLabelsReturnID(strAttrLabels);
                //把属性---保存到关系型数据库
                EntAttribType usrEntAttrib = new EntAttribType();
                usrEntAttrib.setAtbSID(String.valueOf(ids));
                usrEntAttrib.setAtbName(jAttrName);
                usrEntAttrib.setDtpSID("");
                usrEntAttrib.setObjSID("");
                usrEntAttrib.setObjType("");
                usrEntAttrib.setUntName("");
                usrEntAttrib.setUsrSID(userId);
                usrEntAttrib.setEntSID(String.valueOf(entID));
                modelMapper.saveEntClassAttrInfo(usrEntAttrib);

                //MATCH (n)DETACH DELETE n
                //建立关系
                String strAttrRelationships = "match (m) where id(m)=" + nodeID + " match (n) where id(n)=" + ids + " merge(m)-[r:包含 {name:'" + relationName + "',from:id(m),to:id(n)}]->(n)   RETURN id(r)  as id";
                long idRel = neo4jDriverService.saveLabelsReturnID(strAttrRelationships);
                //把关系---保存到关系型数据库
                EntRelType usrEntRel = new EntRelType();
                usrEntRel.setRtpSID(String.valueOf(idRel));
                usrEntRel.setRtpName(relationName);
                usrEntRel.setUsrSID(userId);
                usrEntRel.setEntSID(String.valueOf(entID));
                modelMapper.saveEntClassRelInfo(usrEntRel);

//                //给关系赋值
//                String strRelationshipsValue = "match (s)-[r:包含]->(e) set r.from = id(s),r.to = id(e)";
//                neo4jDriverService.saveLabels(strRelationshipsValue);
                if (jsonArrayDefine.getJSONObject(i).get("children").equals("null")) {
                    System.out.println("Children is null ");
                } else {
                    String jAttrChildren = jsonArrayDefine.getJSONObject(i).getString("children");
                    System.out.println("Children: " + jAttrChildren);
                    JSONArray jAttrChildrenObject = JSONArray.fromObject(jAttrChildren);
                    if (jAttrChildrenObject.size() > 0) {
                        String relationNameAndSx = "属性_包含_属性";
                        this.treeMenuList(userId, jAttrChildrenObject, ids, entID, strRelationName, relationNameAndSx);
                    }
                }
            }
            //给节点加parentId 赋值
//            String strRelationshipsValue = "MATCH (m)-[r]->(n) set n.parentId = id(m)";
//            neo4jDriverService.saveLabels(strRelationshipsValue);
        }
    }

    //1.根据当前用户，查询该用户下的实体类型
    public List<Long> getUsrGraphEntityTypeList(String usrID) {

        List<Map<String, String>> mapList = modelMapper.getUsrGraphEntityTypeList(usrID);
        List<Long> longList = ArrayUtils.getMapListToListLong(mapList);
        return longList;
    }

    @Override
    public Map<String, Object> getAttributesPercent(String mid, String userId) {

        //1.实体类-包含属性，以外的属性
        //a.根据当前用户，查询实体类-下的属性节点
        List<Map<String, String>> mapList = modelMapper.getUsrGraphEntityAttrList(userId, mid);
        List<Long> longList = ArrayUtils.getMapListToListLong(mapList);

        //b.实体类下面的属性
        List<ResultheadData> treeList = entitysRepository.findTreeDataById(Long.valueOf(mid), ArrayUtils.listDataToLong(longList));

        List<String> listEntityClass = this.getEntityAttrName(treeList, Long.valueOf(mid));
        log.info("listEntityClass_trees_:" + listEntityClass);
        //2.实体类-包含属性，以外的属性
        //a.当前用户、此实体类下面的实体
        List<Map<String, String>> entityList = entityMapper.getUsrGraphEntityListByUsrIDAndEntTypeID(userId, mid);

        //b.实体下面的属性
        List<ResultheadData> treeEntityList = entitysRepository.findEntityTreeDataById(ArrayUtils.listDataToLong(ArrayUtils.getMapListToListLong(entityList)));
        List<String> listEntity = this.getEntityAttrName(treeEntityList, Long.valueOf(mid));
        log.info("listEntity_trees_:" + listEntity);

        //计算重复项出现次数
        Map mapAttrNum = ArrayUtils.getListNum(listEntity);
        log.info("计算重复项出现次数:" + mapAttrNum);
        //求两个list 差集
        listEntity.removeAll(listEntityClass);

        //求两个list 去重
        List<String> listAttrName = ArrayUtils.removeDuplicate(listEntity);

        //此实体类下的实体个数
        long entityCnt = entitysRepository.findEntitysCount(Long.valueOf(mid));
        //百分比
        List<String> stringList = new ArrayList<>();
        //组装树形结构
        for (int i = 0; i < listAttrName.size(); i++) {
            String str = listAttrName.get(i);
            String strAttr[] = str.split("/");
            for (String str1 : strAttr) {
                if (!"".equals(str1)) {
                    stringList.add(str1);
                }
            }
        }
        //转数组
        String[] strArrtName = ArrayUtils.listDataToString(stringList);

        //查询属性节点
        List<Long> longEntityList = this.getEntityAttrId(treeEntityList);
        List<Long> newEntityAttrID = ArrayUtils.removeDuplicate(longEntityList);
        log.info("listEntity_trees_AttrID:" + longEntityList);
        List<ResultheadData> treeEntityNameList = entitysRepository.findEntityTreeDataByIdAndName(strArrtName, ArrayUtils.listDataToLong(newEntityAttrID));

        //组装新的tree
        List<AttrTreeNode> attrTreeNodeList = this.getEntitysAttrTreeInfo(ArrayUtils.listDataToLong(ArrayUtils.getMapListToListLong(entityList)), treeEntityNameList);

        //新的tree 中，去掉重复attrname
        List<AttrTreeNode> removeDuplicateAttrNameTree = ArrayUtils.removeDuplicateAttrNameTree(attrTreeNodeList);


        //给新的tree,赋值百分比
        List<AttrTreeNode> newAttrTreeNodeList =  TreeBuilder.buildReplacePercentByNoParentIdRecursive(removeDuplicateAttrNameTree,entityCnt,mapAttrNum);

        Map<String, Object> mapTree = new HashMap<>();

        mapTree.put("id", mid);
        mapTree.put("name", "实体类");
        mapTree.put("define", newAttrTreeNodeList);

        return mapTree;
    }


    /**
     * 属性name
     *
     * @param treeList
     * @param mid
     * @return
     */
    public List<String> getEntityAttrName(List<ResultheadData> treeList, long mid) {
        Map<String, Object> childrenMap = new HashMap<>();
        List<TreeNode> listNode = new ArrayList<TreeNode>();
        for (int i = 0; i < treeList.size(); i++) {
            ResultheadData resultheadData1 = treeList.get(i);
            for (NodeModel nodesEntity : resultheadData1.getNodes()) {
                if (nodesEntity.getPropertyList().size() > 0) {
                    Object name = nodesEntity.property("name");
                    Long ids = nodesEntity.getId();

                    Integer parentId = (Integer) nodesEntity.property("parentId");
                    if (parentId == null || mid == ids) {
                        parentId = 9999999;
                        mid = 9999999;
                    }
                    TreeNode treeNode = new TreeNode(ids, (String) name, parentId.longValue(), null);
                    listNode.add(treeNode);
                }
            }
        }
        List list = ArrayUtils.removeDuplicateTreeNode(listNode);
        List<TreeNode> treeAttrs = TreeBuilder.bulidAttr(list, mid);

        List<String> trees_ = TreeBuilder.buildAttrsByRecursive(treeAttrs, mid);

        return trees_;

    }


    /**
     * 属性id
     *
     * @param treeList
     * @return
     */
    public List<Long> getEntityAttrId(List<ResultheadData> treeList) {
        Map<String, Object> childrenMap = new HashMap<>();
        List<Long> list = new ArrayList<Long>();
        for (int i = 0; i < treeList.size(); i++) {
            ResultheadData resultheadData1 = treeList.get(i);
            for (NodeModel nodesEntity : resultheadData1.getNodes()) {
                if (nodesEntity.getPropertyList().size() > 0) {
                    Long ids = nodesEntity.getId();
                    list.add(ids);
                }
            }
        }
        return list;
    }


    public List<AttrTreeNode> getEntitysAttrTreeInfo(long[] attrId, List<ResultheadData> treeList) {
        List<List<TreeNode>> defineList = new ArrayList<>();
        Map<String, Object> childrenMap = new HashMap<>();
        List<AttrTreeNode> listNode = new ArrayList<AttrTreeNode>();
        for (int i = 0; i < treeList.size(); i++) {
            ResultheadData resultheadData1 = treeList.get(i);
            for (NodeModel nodesEntity : resultheadData1.getNodes()) {
                if (nodesEntity.getPropertyList().size() > 0) {
                    Object name = nodesEntity.property("name");
                    Long ids = nodesEntity.getId();

                    Integer parentId = (Integer) nodesEntity.property("parentId");
                    AttrTreeNode treeNode = new AttrTreeNode(ids, (String) name, parentId.longValue(), 0, new ArrayList<>());
                    listNode.add(treeNode);
                }
            }
        }
        List<AttrTreeNode> trees_ = TreeBuilder.buildByNoParentIdRecursive(ArrayUtils.removeDuplicateAttrTreeNode(listNode), attrId);
        List<AttrTreeNode> treeNodes = new ArrayList<>();
        for (AttrTreeNode treeNode : trees_) {
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    @Override
    public List<Map<String,String>> getModelAttrValue(String entsId){
        //获取实体类的属性列表
        List<String> attrList = modelMapper.getModelAttrList(entsId);
        //获取实体列表
        List<String> entityList = modelMapper.getModelEntityList(entsId);
        //结果集
        List<Map<String,String>> list = new ArrayList<>();
        //遍历实体
        for (String entity:entityList) {
            Map<String,String> map = new HashMap<>();
            map.put("ent_NamE",entity);
            //遍历实体类属性
            for (String attr:attrList){
                String value = modelMapper.getModelAttrValue(entsId,entity,attr);
                map.put(attr,value);
            }
            list.add(map);
        }

        return list;
    }
}
