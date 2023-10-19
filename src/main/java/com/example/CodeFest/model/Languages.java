package com.example.CodeFest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "languages")
public class Languages {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private String pageTitle;
    private String pageSubTitle;
    private String coverImageUrl;

}
