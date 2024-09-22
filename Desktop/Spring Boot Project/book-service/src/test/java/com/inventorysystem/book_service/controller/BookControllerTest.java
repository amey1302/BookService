package com.inventorysystem.book_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventorysystem.book_service.dto.BookRequest;
import com.inventorysystem.book_service.dto.BookResponse;
import com.inventorysystem.book_service.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    BookService bookService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldBeAbleToGetAllBooks() throws Exception {
        // arrange
        List<BookResponse> books = Arrays.asList(
                new BookResponse("adfdsaf", "Clean Code", "ABc", "pub", BigDecimal.valueOf(400), true),
                new BookResponse("dafdas", "Clean Car", "bac", "pub2", BigDecimal.valueOf(200), true)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("adfdsaf"));
    }

    @Test
    void shouldBeAbleToGetBookName() throws Exception {
        // arrange
        BookResponse book = new BookResponse("adfdsaf", "Clean Code",
                "ABc", "pub", BigDecimal.valueOf(400), true);
        String bookName = "Clean Code";

        when(bookService.getBookByName(bookName)).thenReturn(Optional.of(book));

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("adfdsaf"))
                .andExpect(jsonPath("$.publication").value("pub"));
    }

    @Test
    void shouldBeAbleToCreateBook() throws Exception {
        // arrange
        BookRequest book = new BookRequest("Clean Code",
                "ABc", "pub", BigDecimal.valueOf(400), true);
        BookResponse bookResponse = new BookResponse("adfdsaf", "Clean Code",
                "ABc", "pub", BigDecimal.valueOf(400), true);

        when(bookService.saveBook(any(BookRequest.class))).thenReturn(bookResponse);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Clean Code"))
                .andExpect(jsonPath("$.publication").value("pub"));
    }

    @Test
    void shouldBeAbleToAbleUpdateBook() throws Exception {
        // arrange
        BookRequest bookRequest = new BookRequest("Clean Code",
                "ABc", "pub", BigDecimal.valueOf(400), true);
        String bookName = "Clean Code";
        BookResponse updatedBook = new BookResponse("adfdsaf", "Bad Code",
                "ABc", "pub", BigDecimal.valueOf(400), true);

        when(bookService.updateBook(bookName, bookRequest)).thenReturn(updatedBook);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/book")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bad Code"))
                .andExpect(jsonPath("$.publication").value("pub"));
    }

    @Test
    void shouldBeAbleToAbleDeleteBookByName() throws Exception {
        // arrange
        String bookName = "Clean Code";
        String response = "Book is Deleted Successfully!!";

        when(bookService.deleteByName(bookName)).thenReturn(response);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/book")
                        .param("name", bookName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(content().string("Book is Deleted Successfully!!"));
    }


    @Test
    void shouldBeAbleToDeleteAllBooks() throws Exception {
        List<BookResponse> books = Arrays.asList(
                new BookResponse("adfdsaf", "Clean Code", "ABc", "pub", BigDecimal.valueOf(400), true),
                new BookResponse("dafdas", "Clean Car", "bac", "pub2", BigDecimal.valueOf(200), true)
        );
        String response = "All Book Successfully!!";

        when(bookService.deleteAll()).thenReturn(response);

        // act & assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All Book Successfully!!"));
    }


}