package io.vicarius.esbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "samples")
public class SampleDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    String name;

    @Field(type = FieldType.Text, name = "contents")
    String contents;

    @Field(type = FieldType.Date, name = "timestamp")
    Date timestamp = new Date();
}
