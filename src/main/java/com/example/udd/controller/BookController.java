package com.example.udd.controller;

import com.example.udd.dto.DocumentDTO;
import com.example.udd.dto.DownloadBookDTO;
import com.example.udd.dto.PointDTO;
import com.example.udd.dto.SearchFieldDTO;
import com.example.udd.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/book")
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping(value = "/add")
    public ResponseEntity<?> addBook(@ModelAttribute DocumentDTO documentDTO) throws IOException {
        return bookService.addDocument(documentDTO);
    }

    @GetMapping(value = "/getAllBooks")
    public ResponseEntity<?> getAllBooks() throws IOException {
        return bookService.getAllBooks();
    }

    @PostMapping(value = "/downloadBook")
    public ResponseEntity<?> getBetaReaders(@RequestBody DownloadBookDTO downloadBookDTO) throws IOException {
        return bookService.download(downloadBookDTO.getBookName());
    }

    @PostMapping(value = "/search")
    public ResponseEntity<?> searchBooks(@RequestBody HashMap<String, SearchFieldDTO> searchFields) throws  IOException {
        return bookService.search(searchFields);
    }
}
