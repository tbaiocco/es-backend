package io.vicarius.esbackend.controller;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vicarius.esbackend.model.SampleDocument;
import io.vicarius.esbackend.service.IndexService;
import io.vicarius.esbackend.service.SampleDocumentService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SampleDocumentController {

    private final SampleDocumentService service;
    private final IndexService indexService;

    public SampleDocumentController(SampleDocumentService service, IndexService indexService) {
        this.service = service;
        this.indexService = indexService;
    }

    @Operation(summary= "Create new Document into an existing index", description= "Index must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = IndexInformation.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Server-side Error, check the logs")})
    @PostMapping("/{idxName}/document")
    public ResponseEntity<?> create(@PathVariable String idxName, @RequestBody SampleDocument document) {

        if(StringUtils.isNotEmpty(idxName) && Boolean.TRUE.equals(indexService.indexExists(idxName))) {
            return ResponseEntity.ok(service.createDocumentIntoIndex(document, idxName));
        } else {
            return ResponseEntity.badRequest().body("Index not existing, document not saved");
        }
    }

    @Operation(summary= "Get document by id from an index", description= "Index must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = SampleDocument.class)) }),
            @ApiResponse(responseCode = "404", description = "Index not found"),
            @ApiResponse(responseCode = "500", description = "Server-side Error, check the logs")})
    @GetMapping("/{idxName}/document/{id}")
    public ResponseEntity<?> getDocument(@PathVariable String idxName, @PathVariable String id) {
        try {
            if(StringUtils.isNotEmpty(idxName) && Boolean.TRUE.equals(indexService.indexExists(idxName))) {
                SampleDocument result = service.getDocumentFromIndexById(idxName, id);

                if(ObjectUtils.isEmpty(result)) {
                    return ResponseEntity.badRequest().body("Document not found in the index [" + idxName + "]");
                }

                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body("Index not existing, no document to get");
            }
        } catch (ElasticsearchException ese) {
            return ResponseEntity.internalServerError().body(ese.response());
        }

    }
}
