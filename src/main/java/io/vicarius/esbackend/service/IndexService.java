package io.vicarius.esbackend.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class IndexService {

    private final ElasticsearchOperations operations;

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public IndexService(ElasticsearchOperations operations, ElasticsearchClient elasticsearchClient) {
        this.operations = operations;
        this.elasticsearchClient = elasticsearchClient;
    }

    public IndexInformation createIndex(String idxName) {
        IndexOperations indexOps = operations.indexOps(IndexCoordinates.of(idxName));
        if(!indexOps.exists()) {
            indexOps.create();
        }
        return indexOps.getInformation().getFirst();
    }

    public IndexInformation createIndex() {
        String idxName = "vic.test.".concat(String.valueOf(System.currentTimeMillis()));
        return createIndex(idxName);
    }

    public List<IndexInformation> getIndexDetails(String idxName) {
        IndexOperations indexOps = operations.indexOps(IndexCoordinates.of(idxName));
        return indexOps.getInformation();
    }

    public Boolean indexExists(String idxName) {
        IndexOperations indexOps = operations.indexOps(IndexCoordinates.of(idxName));
        return indexOps.exists();
    }

    public List<IndicesRecord> getAllIndices() throws IOException {
        return elasticsearchClient.cat().indices().valueBody();
    }

}
