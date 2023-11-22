package io.vicarius.esbackend.controller;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.cat.indices.IndicesRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vicarius.esbackend.service.IndexService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class IndexController {

    private final IndexService service;

    public IndexController(IndexService service) {
        this.service = service;
    }

    @Operation(summary= "Create new index, if not existing", description= "Body is plain text for the index name, it is optional and follow the naming conventions of Elasticsearch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = IndexInformation.class)) }),
            @ApiResponse(responseCode = "400", description = "Wrong payload"),
            @ApiResponse(responseCode = "500", description = "Server-side Error, check the logs")})
    @PostMapping
    public ResponseEntity<?> create(@RequestBody(required = false) String idxName) {
        IndexInformation result;
        if(StringUtils.isNotEmpty(idxName)) {
            idxName = StringUtils.trim(idxName);
            idxName = StringUtils.truncate(idxName, 150);
            if(StringUtils.containsAny(idxName, "\\/*? \"<>|,#"))
                return ResponseEntity.badRequest().body("Index not created, cannot include \\, /, *, ?, \", <, >, |, ` ` (space character), ,, #");
            if(StringUtils.startsWithAny(idxName, "-","_","+"))
                return ResponseEntity.badRequest().body("Index not created, cannot start with -, _, +");
            if(StringUtils.equalsAny(idxName, ".", ".."))
                return ResponseEntity.badRequest().body("Index not created, cannot be . or ..");
            result = service.createIndex(StringUtils.lowerCase(idxName));
        } else {
            result = service.createIndex();
        }

        return ResponseEntity.ok(result);
    }

    @Operation(summary= "Get index details by name", description= "Index must exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    { @Content(mediaType = "application/json", schema =
                    @Schema(implementation = IndexInformation.class)) }),
            @ApiResponse(responseCode = "404", description = "Index not found"),
            @ApiResponse(responseCode = "500", description = "Server-side Error, check the logs")})
    @GetMapping("/{idxName}")
    public ResponseEntity<?> getIndexDetails(@PathVariable String idxName) {
        try {
            List<IndexInformation> result = service.getIndexDetails(idxName);

            return ResponseEntity.ok(result);
        } catch (NoSuchIndexException nsie) {
            return ResponseEntity.notFound().build();
        } catch (ElasticsearchException ese) {
            return ResponseEntity.internalServerError().body(ese.response());
        }
    }

    @Operation(summary= "Get all indices on the current elasticsearch instance")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", description = "Server-side Error, check the logs")})
    @GetMapping
    public ResponseEntity<?> getAllIndices() {
        try {
            List<IndicesRecord> result = service.getAllIndices();
            List<Map<String, String>> output = new ArrayList<>();
            result.forEach(indicesRecord -> output.add(buildLine(indicesRecord)));

            return ResponseEntity.ok(output);
        } catch (IOException ioe) {
            return ResponseEntity.internalServerError().body(ioe.getMessage());
        }
    }

    private Map<String, String> buildLine(IndicesRecord record) {
        Map<String, String> map = new HashMap<>();
        map.put("name", record.index());
        map.put("num of docs", record.docsCount());
        map.put("uuid", record.uuid());
        map.put("status", record.status());
        return map;
    }
}
