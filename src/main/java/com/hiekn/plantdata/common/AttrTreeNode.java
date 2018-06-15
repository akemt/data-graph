package com.hiekn.plantdata.common;

import java.util.List;

/**
 * Created by Massive on 2016/12/26.
 */
public class AttrTreeNode {

    private long id;

    private long parentId;

    private String name;

    private double percent;

    private String attrname;

    private List<AttrTreeNode> children;

    public AttrTreeNode(long id, String name, long parentId, long percent, List<AttrTreeNode> children) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.percent = percent;
        this.children = children;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<AttrTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<AttrTreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "AttrTreeNode{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                ", name='" + name + '\'' +
                ", attrname='" + attrname + '\'' +
                ", percent='" + percent + '\'' +
                ", children=" + children +
                '}';
    }

}