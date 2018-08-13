package com.hiekn.plantdata.Entity;


import java.util.Date;

/**
 * 实体-实体
 */
public class UsrEntity {

    private String EntSID;
    private String usrSID;
    private String EntName;
    private String EntTmpl;
    private String EntTypeSID;
    private String ENT_DESC;
    private Date CreateDate;

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public String getENT_DESC() {
        return isNotNull(ENT_DESC);
    }

    public void setENT_DESC(String ENT_DESC) {
        this.ENT_DESC = ENT_DESC;
    }

    public String getEntTypeSID() {
        return EntTypeSID;
    }

    public void setEntTypeSID(String entTypeSID) {
        EntTypeSID = entTypeSID;
    }

    public String getEntSID() {
        return EntSID;
    }

    public void setEntSID(String entSID) {
        EntSID = entSID;
    }

    public String getUsrSID() {
        return usrSID;
    }

    public void setUsrSID(String usrSID) {
        this.usrSID = usrSID;
    }

    public String getEntName() {
        return EntName;
    }

    public void setEntName(String entName) {
        EntName = entName;
    }

    public String getEntTmpl() {
        return EntTmpl;
    }

    public void setEntTmpl(String entTmpl) {
        EntTmpl = entTmpl;
    }

    public String isNotNull(String data) {
        return data == null ? "" : data;
    }
}
