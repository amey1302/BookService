package com.inventorysystem.book_service.controller;

import com.inventorysystem.book_service.dto.BookRequest;
import com.inventorysystem.book_service.dto.BookResponse;
import com.inventorysystem.book_service.exception.BookWithNameNotFoundException;
import com.inventorysystem.book_service.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookservice;

    @GetMapping("/books")
    public ResponseEntity<List<BookResponse>> getAlLBooks() {
        return new ResponseEntity<>(bookservice.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/book")
    public ResponseEntity<Optional<BookResponse>> getBookByName(@RequestParam("name") String name) {
        try {
            return new ResponseEntity<>(bookservice.getBookByName(name), HttpStatus.OK);
        } catch (BookWithNameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/book")
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookRequest) {

        return new ResponseEntity<>(bookservice.saveBook(bookRequest), HttpStatus.CREATED);
    }
    @PutMapping("/book")
    public ResponseEntity<BookResponse> updateBook(@RequestParam("name") String name, @RequestBody BookRequest bookRequest) {
        return new ResponseEntity<>(bookservice.updateBook(name, bookRequest), HttpStatus.OK);
    }

    @DeleteMapping("/book")
    public ResponseEntity<String> deleteBookByName(@RequestParam("name") String name) {
        return new ResponseEntity<>(bookservice.deleteByName(name), HttpStatus.OK);
    }

    @DeleteMapping("/books")
    public ResponseEntity<?> deleteAll() {
        return new ResponseEntity<>(bookservice.deleteAll(), HttpStatus.OK);
    }
}
