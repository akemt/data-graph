package com.hiekn.plantdata.Entity;

public class Datasource {
    private String datasourceid;

    private String datasourcename;

    private String classid;

    private String kind;

    private String config;

    public String getDatasourceid() {
        return datasourceid;
    }

    public void setDatasourceid(String datasourceid) {
        this.datasourceid = datasourceid == null ? null : datasourceid.trim();
    }

    public String getDatasourcename() {
        return datasourcename;
    }

    public void setDatasourcename(String datasourcename) {
        this.datasourcename = datasourcename == null ? null : datasourcename.trim();
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid == null ? null : classid.trim();
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind == null ? null : kind.trim();
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config == null ? null : config.trim();
    }
}