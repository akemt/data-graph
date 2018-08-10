package com.hiekn.plantdata.Entity;

/**
 * 码表、同义词表导入结果
 */
public class ImportResult {

    private String datasourceId;
    private Integer match;
    private Integer nomatch;
    private Boolean status;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public Integer getMatch() {
        return match;
    }

    public void setMatch(Integer match) {
        this.match = match;
    }

    public Integer getNomatch() {
        return nomatch;
    }

    public void setNomatch(Integer nomatch) {
        this.nomatch = nomatch;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
