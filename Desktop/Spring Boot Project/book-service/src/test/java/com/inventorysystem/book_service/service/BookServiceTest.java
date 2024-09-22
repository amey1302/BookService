package com.inventorysystem.book_service.service;

import com.inventorysystem.book_service.dto.BookRequest;
import com.inventorysystem.book_service.dto.BookResponse;
import com.inventorysystem.book_service.model.Book;
import com.inventorysystem.book_service.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldBeAbleToGetAllBooks() {
        // arrange
        Mockito.when(bookRepository.findAll()).thenReturn(List.of());

        // act & assert
        Assertions.assertEquals(List.of(), bookService.getAllBooks());
    }

    @Test
    void shouldBeAbleToGetBookByName() {
        // arrange
        String bookName = "DDD";

        Mockito.when(bookRepository.findByName(bookName)).thenReturn(Optional.empty());

        // act & assert
        Assertions.assertEquals(List.of(), bookService.getAllBooks());
    }

    @Test
    void shouldBeAbleToSaveTheBook() {
        // arrange
        BookRequest bookRequest = new BookRequest("DDD", "author", "pub", BigDecimal.valueOf(100), true);
        BookResponse expectedBookResponse =
                new BookResponse("idgenerated", "DDD", "author", "pub", BigDecimal.valueOf(100), true);

        // act
        Book book = Book.builder()
                .name(bookRequest.getName())
                .author(bookRequest.getAuthor())
                .publication(bookRequest.getPublication())
                .price(bookRequest.getPrice())
                .build();

        Mockito.when(bookRepository.save(book)).thenReturn(book);
        BookResponse actual = bookService.saveBook(bookRequest);

        // assert
        Assertions.assertEquals(expectedBookResponse.getName(), actual.getName());
        Assertions.assertEquals(expectedBookResponse.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expectedBookResponse.getPublication(), actual.getPublication());
        Assertions.assertEquals(expectedBookResponse.getPrice(), actual.getPrice());
    }

    private BookResponse mapToDTO(Book book) {
        return BookResponse.builder().id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .publication(book.getPublication())
                .price(book.getPrice())
                .build();
    }
}