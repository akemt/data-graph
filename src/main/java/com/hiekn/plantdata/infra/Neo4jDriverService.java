package com.hiekn.plantdata.infra;

import org.neo4j.driver.v1.StatementResult;

/**
 * neo4j数据库驱动
 */
public interface Neo4jDriverService {


    /**
     * 保存，并返回ID
     * @param cql
     * @return
     */
    public long saveLabelsReturnID(String cql);

    /**
     * 保存节点或者关系
     * @param cql
     */
    public void saveLabels(String cql);

    /**
     * 获取neo4j数据库信息
     * @param cql
     * @return
     */
    public StatementResult getNeo4jDetail(String cql);
}
