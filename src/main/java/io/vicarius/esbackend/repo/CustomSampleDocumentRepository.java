package io.vicarius.esbackend.repo;

import io.vicarius.esbackend.model.SampleDocument;

import java.util.List;

public interface CustomSampleDocumentRepository {

    SampleDocument createDocumentIntoIndex(SampleDocument sampleDocument, String idxName);

    SampleDocument getDocumentFromIndexById(String idxName, String id);

    List<SampleDocument> findAllFromIndex(String idxName);

}
