package com.kjs990114.goodong.document;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Data
@Getter
@Setter
@Document(indexName = "post", writeTypeHint = WriteTypeHint.FALSE)
public class PostDocument {

    @Id
    private String id;

    private Long postId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

}
