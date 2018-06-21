package com.hiekn.plantdata.util;

import com.hiekn.plantdata.Entity.Enterprise;
import com.hiekn.plantdata.bean.item.RelationItem;
import com.hiekn.plantdata.common.AttrTreeNode;
import com.hiekn.plantdata.common.TreeNode;
import org.neo4j.ogm.model.Property;
import org.neo4j.ogm.response.model.RelationshipModel;

import java.util.*;

/**
 * 数组转换
 */
public class ArrayUtils {

    /**
     * List<long> 转Long数组
     *
     * @param list
     * @return
     */
    public static long[] listDataToLong(List<Long> list) {
        long[] longs = new long[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            longs[i] = list.get(i);
        }
        return longs;
    }

    public static String listDataToStr(List<Long> list) {
        String str = "";
        for (int i = 0; i < list.size(); ++i) {
            str += list.get(i) + ",";
        }
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    /**
     * List<String> 转String数组
     *
     * @param list
     * @return
     */
    public static String[] listDataToString(List<String> list) {
        String[] longs = new String[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            longs[i] = list.get(i);
        }
        return longs;
    }

    /**
     * 转String数组
     *
     * @param distance
     * @return
     */
    public static String[] intDataToString(Integer distance) {
        String[] longs = new String[1];
        if (distance == 1) {
            longs[0] = "*1";
        } else {
            longs[0] = "*1.." + distance;
        }
        longs[0] = longs[0].replaceAll("\"", "");
        return longs;
    }


    /**
     * 删除ArrayList中重复元素
     *
     * @param list
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }


    /**
     * 通过Hashset的add方法判断是否已经添加过相同的数据，如果已存在相同的数据则不添加
     *
     * @param arlList
     */
    public static List removeDuplicateEnterprise(List<Enterprise> arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); ) {
            Enterprise element = (Enterprise) iter.next();
            if (set.add(element.getId()))
                newList.add(element);
        }
        arlList.clear();
        arlList.addAll(newList);
        return arlList;
    }

    public static List removeDuplicateRelationshipModel(List<RelationItem> arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); ) {
            RelationItem element = (RelationItem) iter.next();
            if (set.add(element.getId()))
                newList.add(element);
        }
        arlList.clear();
        arlList.addAll(newList);
        return arlList;
    }


    //  删除ArrayList中重复元素 
    public static List removeDuplicateList(List<Enterprise> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).toString().equals(list.get(i).toString())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }


    /**
     * 遍历RelationshipModel 值
     *
     * @param properties
     * @param key
     * @return
     */
    public static Object property(List<Property<String, Object>> properties, String key) {
        Iterator i$ = properties.iterator();

        Property property;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            property = (Property) i$.next();
        } while (!property.getKey().equals(key));

        return property.getValue();
    }


    public static List removeDuplicateTreeNode(List<TreeNode> arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); ) {
            TreeNode element = (TreeNode) iter.next();
            if (set.add(element.getId()))
                newList.add(element);
        }
        arlList.clear();
        arlList.addAll(newList);
        return arlList;
    }

    public static List removeDuplicateAttrTreeNode(List<AttrTreeNode> arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); ) {
            AttrTreeNode element = (AttrTreeNode) iter.next();
            if (set.add(element.getId()))
                newList.add(element);
        }
        arlList.clear();
        arlList.addAll(newList);
        return arlList;
    }


    /**
     * 去掉
     *
     * @param arlList
     * @return
     */
    public static List removeDuplicateAttrNameTree(List<AttrTreeNode> arlList) {
        Set set = new HashSet();
        List newList = new ArrayList();
        Map map = new HashMap();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); ) {
            AttrTreeNode element = (AttrTreeNode) iter.next();
            if (set.add(element.getAttrname())) {
                newList.add(element);
                map.put(element.getAttrname(), element.getChildren());
            } else {
                if (element.getChildren().size() > 0) {
                    removeDuplicateAttrChildrenTree((List<AttrTreeNode>) map.get(element.getAttrname()), element.getChildren());
                }
            }
        }
        arlList.clear();
        arlList.addAll(newList);
        List newDupList = removeDuplicateChildrenList(arlList);
        return newDupList;
    }

    public static List removeDuplicateAttrChildrenTree(List<AttrTreeNode> childList, List<AttrTreeNode> arlList) {
        childList.addAll(arlList);
        Set set = new HashSet();
        Map map = new HashMap();
        for (Iterator iter = childList.iterator(); iter.hasNext(); ) {
            AttrTreeNode element = (AttrTreeNode) iter.next();
            if (set.add(element.getAttrname())) {
                map.put(element.getAttrname(), element.getChildren());
            } else {
//                removeDuplicateChildrenList(childList);
//                childList.add(element);
                if (element.getChildren().size() > 0) {
                    removeDuplicateAttrChildrenTree((List<AttrTreeNode>) map.get(element.getAttrname()), element.getChildren());
                }
            }
        }
        return childList;
    }


    //  删除ArrayList中重复元素
    public static List removeDuplicateChildrenList(List<AttrTreeNode> arlList) {
        List newList = new ArrayList();
        for (Iterator iter = arlList.iterator(); iter.hasNext(); ) {
            AttrTreeNode element = (AttrTreeNode) iter.next();
            if (element.getChildren().size() > 0) {
                removeDuplicateChildrenList11(element.getChildren());
            }
            newList.add(element);
        }
        return newList;
    }

    public static void removeDuplicateChildrenList11(List<AttrTreeNode> arlList) {
        Set set = new HashSet();
        for (int i = 0; i < arlList.size(); i++) {
            AttrTreeNode element = (AttrTreeNode) arlList.get(i);
            if (!set.add(element.getAttrname())) {
                arlList.remove(element);
            }
        }
    }

    /**
     * 获取List 信息中的id
     *
     * @param mapList
     * @return
     */
    public static List<Long> getMapListToListLong(List<Map<String, String>> mapList) {

        List<Long> longList = new ArrayList<>();
        Map<String, String> map = null;
        for (int i = 0; i < mapList.size(); i++) {
            map = mapList.get(i);
            String strID = map.get("id");
            longList.add(Long.valueOf(strID));
        }
        return longList;
    }

    /**
     * Java List中的重复项出现次数
     *
     * @param trees_
     * @return
     */
    public static Map getListNum(List<String> trees_) {

        Map map = new HashMap();

        for (String temp : trees_) {
            Integer count = (Integer) map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        return map;
    }

}
