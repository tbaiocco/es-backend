package io.vicarius.esbackend.service;

import io.vicarius.esbackend.model.SampleDocument;
import io.vicarius.esbackend.repo.SampleDocumentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class SampleDocumentServiceTest {

    @Autowired
    private SampleDocumentService service;

    @Autowired
    private SampleDocumentRepository repository;

    String idxName = "test-idx";

    @BeforeAll
    static void beforeAll() {

    }

    @Test
    @Order(1)
    void createDocumentIntoIndex() {
        final SampleDocument testSampleDocument = new SampleDocument();
        testSampleDocument.setName("test-create-".concat(String.valueOf(testSampleDocument.getTimestamp())));
        testSampleDocument.setContents("Tested into 'test-idx'");
        SampleDocument created = service.createDocumentIntoIndex(testSampleDocument, idxName);
        Assert.notNull(created, "Creation error for testSampleDocument into index");
        Assert.notNull(created.getId(), "Document id is null!");
    }

    @Test
    @Order(2)
    void getDocumentFromIndexById() {
        final SampleDocument testSampleDocument = new SampleDocument();
        testSampleDocument.setName("test-general-".concat(String.valueOf(testSampleDocument.getTimestamp())));
        testSampleDocument.setContents("Tested into 'test-idx'");
        SampleDocument created = service.createDocumentIntoIndex(testSampleDocument, idxName);
        Assert.notNull(created, "Creation error for testSampleDocument into index");
        Assert.notNull(created.getId(), "Document id is null!");

        SampleDocument result = service.getDocumentFromIndexById(idxName, created.getId());
        Assert.notNull(result, "Retrieve error for index");
        Assert.notNull(result.getId(), "Document result id is null!");
        Assert.isTrue(result.getName().equals(created.getName()), "Documents are different");

    }

    @Test
    @Order(3)
    void getDocumentById() {
        final SampleDocument testSampleDocument = new SampleDocument();
        testSampleDocument.setName("test-sampleIdx-".concat(String.valueOf(testSampleDocument.getTimestamp())));
        testSampleDocument.setContents("Tested into 'test-idx'");
        SampleDocument defaultDoc = repository.save(testSampleDocument);
        Assert.notNull(defaultDoc, "Creation error for testSampleDocument into default index");
        Assert.notNull(defaultDoc.getId(), "Document id is null!");

        SampleDocument result = service.getDocumentById(defaultDoc.getId());
        Assert.notNull(result, "Failed to fetch test doc from default index");
    }

    @Test
    @Order(4)
    void findAllFromIndex() {
        List<SampleDocument> documents = service.findAllFromIndex(idxName);
        Assert.notEmpty(documents, "Failed to fetch documents from index [" + idxName + "]");
    }

    @Test
    @Order(5)
    void findAllFromDefaultIndex() {
        Iterable<SampleDocument> documents = service.findAll();
        List<SampleDocument> result = new ArrayList<>();
        documents.forEach(result::add);
        Assert.notEmpty(result, "Failed to fetch documents from default index");
    }

}