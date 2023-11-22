package io.vicarius.esbackend.service;

import io.vicarius.esbackend.model.SampleDocument;
import io.vicarius.esbackend.repo.SampleDocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleDocumentService {

    private final SampleDocumentRepository sampleDocumentRepository;

    public SampleDocumentService(SampleDocumentRepository sampleDocumentRepository) {
        this.sampleDocumentRepository = sampleDocumentRepository;
    }

    public SampleDocument createDocumentIntoIndex(SampleDocument sampleDocument, String idxName) {
        return sampleDocumentRepository.createDocumentIntoIndex(sampleDocument, idxName);
    }

    public SampleDocument getDocumentFromIndexById(String idxName, String id) {
        return sampleDocumentRepository.getDocumentFromIndexById(idxName, id);
    }

    public SampleDocument getDocumentById(String id) {
        return sampleDocumentRepository.findById(id).orElse(new SampleDocument());
    }

    public List<SampleDocument> findAllFromIndex(String idxName) {
        return sampleDocumentRepository.findAllFromIndex(idxName);
    }

    public Iterable<SampleDocument> findAll() {
        return sampleDocumentRepository.findAll();
    }
}
