package io.vicarius.esbackend.repo;

import io.vicarius.esbackend.model.SampleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleDocumentRepository extends ElasticsearchRepository<SampleDocument, String>, CustomSampleDocumentRepository {
}
