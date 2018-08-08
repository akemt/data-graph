package com.hiekn.plantdata.Entity;

public class Synonym {
    private String synid;

    private String codeid;

    private String synname;

    public String getSynid() {
        return synid;
    }

    public void setSynid(String synid) {
        this.synid = synid == null ? null : synid.trim();
    }

    public String getCodeid() {
        return codeid;
    }

    public void setCodeid(String codeid) {
        this.codeid = codeid == null ? null : codeid.trim();
    }

    public String getSynname() {
        return synname;
    }

    public void setSynname(String synname) {
        this.synname = synname == null ? null : synname.trim();
    }
}