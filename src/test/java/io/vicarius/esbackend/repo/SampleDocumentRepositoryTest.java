package io.vicarius.esbackend.repo;

import io.vicarius.esbackend.model.SampleDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SampleDocumentRepositoryTest {

    @Autowired
    private SampleDocumentRepository repository;

    @Test
    void save() {
        final SampleDocument testSampleDocument = new SampleDocument();
        testSampleDocument.setId("6ePu8osBi7VYPpfyKoBg");
        testSampleDocument.setName("test run");
        testSampleDocument.setContents("run auto test for validation at: ".concat(String.valueOf(testSampleDocument.getTimestamp())));
        final SampleDocument saved = repository.save(testSampleDocument);

        Assertions.assertNotNull(saved);
    }
}