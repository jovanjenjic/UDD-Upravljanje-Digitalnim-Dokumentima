package com.example.udd.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
@NoArgsConstructor
public class Book {
    public static final String INDEX_NAME = "book";

    private String id;
    private String title;
    private String isbn;
    private String authorFirtsName;
    private String authorLastName;
    private Long authorId;
    private String content;
    private String genre;
    private GeoPoint point;
}
