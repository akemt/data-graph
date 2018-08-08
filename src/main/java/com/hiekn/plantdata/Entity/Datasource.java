package com.hiekn.plantdata.Entity;

public class Datasource {
    private String datasourceId;

    private String datasourceName;

    private String classNd;

    private String kind;

    private String config;

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

    public String getClassNd() {
        return classNd;
    }

    public void setClassNd(String classNd) {
        this.classNd = classNd;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}