package com.hiekn.plantdata.infra.impl;

import com.hiekn.plantdata.Entity.Enterprise;
import com.hiekn.plantdata.Entity.Neo4jEntity;
import com.hiekn.plantdata.Entity.ResultheadData;
import com.hiekn.plantdata.dao.Neo4jRepository;
import com.hiekn.plantdata.infra.Neo4jDriverService;
import org.neo4j.driver.internal.InternalRecord;
import org.neo4j.driver.internal.value.InternalValue;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.ogm.response.model.NodeModel;
import org.springframework.stereotype.Service;
import org.neo4j.driver.internal.value.IntegerValue;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class Neo4jDriverServiceImpl implements Neo4jDriverService {

    @Autowired
    private Neo4jEntity neo4jEntity;


    public Neo4jRepository getNeo4jConnect(){
        Neo4jRepository example = new Neo4jRepository(neo4jEntity.getUri(),
                neo4jEntity.getUsername(), neo4jEntity.getPassword());
        return example;
    }

    public long saveLabelsReturnID(String cql){

        Neo4jRepository example = this.getNeo4jConnect();
        StatementResult statementResult =  example.saveLabels(cql);
        List<Record> recordList = statementResult.list();
        Record record = (Record)recordList.get(0);
        IntegerValue ids = (IntegerValue)record.get("id");
        example.close();
        return ids.asLong();
    }

    public void saveLabels(String cql){

        Neo4jRepository example = this.getNeo4jConnect();
        StatementResult statementResult =  example.saveLabels(cql);
        example.close();
    }

    public StatementResult getNeo4jDetail(String cql){
        Neo4jRepository example = this.getNeo4jConnect();
       return example.getNeo4jDetail(cql);
    }
}
