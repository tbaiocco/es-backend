package io.vicarius.esbackend.repo;

import io.vicarius.esbackend.model.SampleDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CustomSampleDocumentRepositoryImpl implements CustomSampleDocumentRepository {

    private final ElasticsearchOperations operations;

    public CustomSampleDocumentRepositoryImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @Override
    public SampleDocument createDocumentIntoIndex(SampleDocument sampleDocument, String idxName) {
        return operations.save(sampleDocument, IndexCoordinates.of(idxName));
    }

    @Override
    public SampleDocument getDocumentFromIndexById(String idxName, String id) {
        return operations.get(id, SampleDocument.class, IndexCoordinates.of(idxName));
    }

    @Override
    public List<SampleDocument> findAllFromIndex(String idxName) {
        Query myQuery = Query.findAll();
        List<SampleDocument> results = new ArrayList<>();
        operations.search(myQuery, SampleDocument.class, IndexCoordinates.of(idxName)).get().forEach(sampleDocumentSearchHit -> results.add(sampleDocumentSearchHit.getContent()));

        return results;
    }
}
