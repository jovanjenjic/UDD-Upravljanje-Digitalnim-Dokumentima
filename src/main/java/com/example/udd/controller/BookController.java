package com.example.udd.controller;

import com.example.udd.dto.DocumentDTO;
import com.example.udd.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
}
