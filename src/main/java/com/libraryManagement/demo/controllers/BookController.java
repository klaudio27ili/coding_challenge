package com.libraryManagement.demo.controllers;

import com.libraryManagement.demo.dtos.BookPostPatchDTO;
import com.libraryManagement.demo.services.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books/")
public class BookController {
    private final BookService bookService;
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody BookPostPatchDTO bookPostPatchDTO) {
        bookService.save(bookPostPatchDTO);
        return ResponseEntity.ok().build();
    }
}
