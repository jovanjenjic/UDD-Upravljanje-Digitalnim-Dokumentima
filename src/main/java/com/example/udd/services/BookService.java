package com.example.udd.services;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    final private static String[] FETCH_FIELDS = { "id", "filename", "content",
    "authorFirstName", "authorLastName", "isbn", "authorId", "title",
            "genre", "point"};
}
