package com.example.udd.services;

import com.example.udd.dto.DocumentDTO;
import com.example.udd.handlers.*;
import com.example.udd.model.Book;
import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BookService {
    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    final private static String[] FETCH_FIELDS = { "id", "filename", "content",
    "authorFirstName", "authorLastName", "isbn", "authorId", "title",
            "genre", "point"};


    public ResponseEntity<?> addDocument(DocumentDTO documentDTO) throws IOException {
        DocumentHandler documentHandler = getHandler(documentDTO.getFile().getOriginalFilename());

        Book book = new Book();

        // Author
        book.setAuthorId(documentDTO.getAuthorId());
        book.setAuthorFirtsName(documentDTO.getAuthorFirstName());
        book.setAuthorLastName(documentDTO.getAuthorLastName());

        // Content
        File convertedFile = convert(documentDTO.getFile());
        book.setContent(documentHandler.getText(convertedFile));

        // Other
        book.setId(documentDTO.getFile().getOriginalFilename());
        book.setGenre(documentDTO.getGenre());
        book.setIsbn(documentDTO.getIsbn());
        book.setTitle(documentDTO.getTitle());
        book.setPoint(new GeoPoint(documentDTO.getLatitude(), documentDTO.getLongitude()));

        IndexRequest indexRequest = new IndexRequest(Book.INDEX_NAME);
        indexRequest.id(book.getId());
        indexRequest.source(new Gson().toJson(book), XContentType.JSON);

        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        return new ResponseEntity<>(indexResponse, HttpStatus.CREATED);
    }

    private File convert(MultipartFile file) throws IOException {
        File convertedFile = new File("pdf" + File.separator + file.getOriginalFilename());
        convertedFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }
    private DocumentHandler getHandler(String fileName) {
        if (fileName.endsWith(".txt")) return new TextDocHandler();
        if (fileName.endsWith(".pdf")) return new PDFHandler();
        if (fileName.endsWith(".doc")) return new WordHandler();
        if (fileName.endsWith(".docs")) return new Word2007Handler();

        return null;
    }

    public ResponseEntity<?> getAllBooks() throws IOException {
        SearchRequest searchRequest = new SearchRequest();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchAllQuery());

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder).fetchSource(FETCH_FIELDS, null);

        searchRequest.indices(Book.INDEX_NAME);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return new ResponseEntity<>(searchResponse.getHits(), HttpStatus.OK);
    }

    public ResponseEntity<?> download(String name) throws IOException {
        Path file = Paths.get( "pdf" ).resolve( name );
        Resource resource = new UrlResource( file.toUri() );

        return ResponseEntity.ok().header( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"" ).body( resource );
    }
}
