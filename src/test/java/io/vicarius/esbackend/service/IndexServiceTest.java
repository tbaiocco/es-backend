package io.vicarius.esbackend.service;

import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
class IndexServiceTest {

    @Autowired
    private IndexService service;

    String idxName = "a-idx-for-test";
    @BeforeAll
    static void beforeAll() {

    }

    @Test
    @Order(1)
    void createIndex() {
        IndexInformation created = service.createIndex();
        Assert.isTrue(created.getName().startsWith("vic.test."), "Creation error for unnamed idx!");
    }

    @Test
    @Order(2)
    void CreateIndexWithName() {
        IndexInformation created = service.createIndex(idxName);
        Assert.isTrue(idxName.equals(created.getName()), "Creation error for idx named " + idxName +"!");
    }

    @Test
    @Order(3)
    void getIndexDetail() {
        List<IndexInformation> result = service.getIndexDetails(idxName);
        Assert.notEmpty(result, "Failed to get details of the index into ES Server");
        Assert.notNull(result.getFirst(), "Failed to get details of the index into ES Server");
    }

    @Test
    @Order(4)
    void indexExists() {
        Assert.isTrue(service.indexExists(idxName), "Failed to check [" + idxName + "] exists in ES Server");
    }

    @SneakyThrows
    @Test
    @Order(5)
    void getAllIndices() {
        List<IndicesRecord> result = service.getAllIndices();
        Assert.notEmpty(result, "Failed to fetch indices from ES Server");
    }

}