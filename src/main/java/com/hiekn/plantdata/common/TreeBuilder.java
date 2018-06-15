package com.hiekn.plantdata.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

/**
 * Created by Massive on 2016/12/26.
 */
public class TreeBuilder {

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static List<TreeNode> bulid(List<TreeNode> treeNodes) {

        List<TreeNode> trees = new ArrayList<TreeNode>();

        for (TreeNode treeNode : treeNodes) {

            if ("0".equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }

            for (TreeNode it : treeNodes) {
                if (it.getParentId() == treeNode.getId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static List<TreeNode> buildByRecursive(List<TreeNode> treeNodes, long parentId) {
        List<TreeNode> trees = new ArrayList<TreeNode>();
        for (TreeNode treeNode : treeNodes) {
            if (parentId == treeNode.getParentId()) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 无父节点-使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static List<AttrTreeNode> buildByNoParentIdRecursive(List<AttrTreeNode> treeNodes, long[] parentId) {
        List<AttrTreeNode> trees = new ArrayList<AttrTreeNode>();
        for (AttrTreeNode treeNode : treeNodes) {
            String strAttr = "/";
            boolean contains = LongStream.of(parentId).anyMatch(x -> x == treeNode.getParentId());
            if (contains) {
                strAttr += treeNode.getName() + "/";
                trees.add(findNoParentChildren(treeNode, treeNodes, strAttr));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static AttrTreeNode findNoParentChildren(AttrTreeNode treeNode, List<AttrTreeNode> treeNodes, String str) {
        for (AttrTreeNode it : treeNodes) {
            if (treeNode.getId() == (it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<AttrTreeNode>());
                }
                String strAttr = str + it.getName() + "/";
                treeNode.setAttrname(strAttr);
                treeNode.getChildren().add(findNoParentChildren(it, treeNodes, strAttr));
            } else {
                treeNode.setAttrname(str);
            }
        }
        return treeNode;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static TreeNode findChildren(TreeNode treeNode, List<TreeNode> treeNodes) {
        for (TreeNode it : treeNodes) {
            if (treeNode.getId() == (it.getParentId())) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<TreeNode>());
                }
                treeNode.getChildren().add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }


    /**
     * 使用递归方法建树--验证属性
     *
     * @param treeNodes
     * @return
     */
    public static List<String> buildAttrsByRecursive(List<TreeNode> treeNodes, long parentId) {
        List<String> trees = new ArrayList<String>();
        for (TreeNode treeNode : treeNodes) {
            String strAttr = "";
            List<String> treeAttr = new ArrayList<String>();
            if (parentId == treeNode.getParentId()) {
                strAttr = "/";
//                strAttr += treeNode.getName()+"/";
                treeAttr = findChildrenByAttrs(treeNode, treeNode.getChildren(), strAttr);
                trees.addAll(treeAttr);
            }
        }
        return trees;
    }


    /**
     * 递归查找子节点--验证属性
     *
     * @param treeNodes
     * @return
     */
    public static List<String> findChildrenByAttrs(TreeNode treeNode, List<TreeNode> treeNodes, String str) {

        List<String> list = new ArrayList<>();
        for (TreeNode it : treeNodes) {
            String strAttr = "";
            List<String> treeAttr = new ArrayList<String>();
            if (treeNode.getId() == (it.getParentId())) {
                strAttr = str + it.getName() + "/";
                list.add(strAttr);
                if (it.getChildren() != null) {
                    treeAttr = findChildrenByAttrs(it, it.getChildren(), strAttr);
                }
                list.addAll(treeAttr);
            }
        }
        return list;
    }

    /**
     * 无父节点-使用递归方法建树-替换里面的值
     *
     * @param attrTreeNodeList
     * @param entityCnt
     * @param mapAttrNum
     * @return
     */
    public static List<AttrTreeNode> buildReplacePercentByNoParentIdRecursive(List<AttrTreeNode> attrTreeNodeList, long entityCnt, Map mapAttrNum) {

        for (AttrTreeNode treeNode : attrTreeNodeList) {
            if (mapAttrNum.containsKey(treeNode.getAttrname())) {

                DecimalFormat df = new DecimalFormat("0.00");

                String attrCnt = String.valueOf(mapAttrNum.get(treeNode.getAttrname()));
                Double percent = Double.valueOf(attrCnt) / entityCnt;

                treeNode.setPercent(Double.valueOf(df.format(percent * 100)));
            }
            if (treeNode.getChildren().size() > 0) {
                buildReplacePercentByNoParentIdRecursive(treeNode.getChildren(), entityCnt, mapAttrNum);
            }
        }
        return attrTreeNodeList;
    }

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static List<TreeNode> bulidAttr(List<TreeNode> treeNodes, long mid) {

        List<TreeNode> trees = new ArrayList<TreeNode>();

        for (TreeNode treeNode : treeNodes) {

            if (mid == treeNode.getParentId()) {
                trees.add(treeNode);
            }

            for (TreeNode it : treeNodes) {
                if (it.getParentId() == treeNode.getId()) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<TreeNode>());
                    }
                    treeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }


    public static void main(String[] args) {

        TreeNode treeNode1 = new TreeNode(1, "广州", 0, null);
        TreeNode treeNode2 = new TreeNode(2, "深圳", 0, null);

        TreeNode treeNode3 = new TreeNode(3, "天河区", 1, null);
        TreeNode treeNode4 = new TreeNode(4, "越秀区", 1, null);
        TreeNode treeNode5 = new TreeNode(5, "黄埔区", 1, null);
        TreeNode treeNode6 = new TreeNode(6, "石牌", 1, null);
        TreeNode treeNode7 = new TreeNode(7, "百脑汇", 1, null);


        TreeNode treeNode8 = new TreeNode(8, "南山区", 2, null);
        TreeNode treeNode9 = new TreeNode(9, "宝安区", 2, null);
        TreeNode treeNode10 = new TreeNode(10, "科技园", 2, null);


        List<TreeNode> list = new ArrayList<TreeNode>();

        list.add(treeNode1);
        list.add(treeNode2);
        list.add(treeNode3);
        list.add(treeNode4);
        list.add(treeNode5);
        list.add(treeNode6);
        list.add(treeNode7);
        list.add(treeNode8);
        list.add(treeNode9);
        list.add(treeNode10);
        List<TreeNode> trees = TreeBuilder.bulidAttr(list, 0);

        String str = "";
        List<String> trees_ = TreeBuilder.buildAttrsByRecursive(trees, 0);
        System.out.println("trees_:" + trees_);

    }


}