package com.example.CodeFest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pdfMaterial")

public class PdfContent {
    @Id
    private String id;
    private String language;
    private String title;
    private String pdfUrl;
    private String vedioUrl;

}
