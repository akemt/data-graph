package com.hiekn.plantdata.Entity;

public class Nomatch {
    private String nomatchid;

    private String classid;

    private String datasourceid;

    private String nomatchname;

    public String getNomatchid() {
        return nomatchid;
    }

    public void setNomatchid(String nomatchid) {
        this.nomatchid = nomatchid == null ? null : nomatchid.trim();
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid == null ? null : classid.trim();
    }

    public String getDatasourceid() {
        return datasourceid;
    }

    public void setDatasourceid(String datasourceid) {
        this.datasourceid = datasourceid == null ? null : datasourceid.trim();
    }

    public String getNomatchname() {
        return nomatchname;
    }

    public void setNomatchname(String nomatchname) {
        this.nomatchname = nomatchname == null ? null : nomatchname.trim();
    }
}