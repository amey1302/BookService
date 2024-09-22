package com.inventorysystem.book_service.service;

import com.inventorysystem.book_service.dto.BookRequest;
import com.inventorysystem.book_service.dto.BookResponse;
import com.inventorysystem.book_service.exception.BookWithNameNotFoundException;
import com.inventorysystem.book_service.model.Book;
import com.inventorysystem.book_service.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    public Optional<BookResponse> getBookByName(String name) throws BookWithNameNotFoundException {
        Optional<Book> optionalBook = bookRepository.findByName(name);
        if (optionalBook.isPresent()) {
            return Optional.of(BookResponse.builder()
                    .id(optionalBook.get().getId())
                    .name(optionalBook.get().getName())
                    .author(optionalBook.get().getAuthor())
                    .publication(optionalBook.get().getPublication())
                    .price(optionalBook.get().getPrice())
                    .availability(optionalBook.get().getAvailability())
                    .build());
        }
        throw new BookWithNameNotFoundException("Book  " + name + " is not found");
    }

    public BookResponse saveBook(BookRequest bookRequest) {
        Book book = Book.builder()
                .name(bookRequest.getName())
                .author(bookRequest.getAuthor())
                .publication(bookRequest.getPublication())
                .price(bookRequest.getPrice())
                .availability(bookRequest.getAvailability())
                .build();
        bookRepository.save(book);
        log.info("Book {} is  Saved", book.getId());
        return mapToDTO(book);
    }

    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookResponse> bookResponses = books.stream().map(this::mapToDTO).toList();
        return bookResponses;
    }


    public BookResponse updateBook(String name, BookRequest bookRequest) {
        Optional<Book> optionalBookRequest = bookRepository.findByName(name);
        if (optionalBookRequest.isPresent()) {
            Book existingBook = optionalBookRequest.get();

            if (!bookRequest.getName().isEmpty() && bookRequest.getName() != null) {
                existingBook.setName(bookRequest.getName());
            }

            if (!bookRequest.getAuthor().isEmpty() && bookRequest.getAuthor() != null) {
                existingBook.setAuthor(bookRequest.getAuthor());
            }
            if (!bookRequest.getPublication().isEmpty() && bookRequest.getPublication() != null) {
                existingBook.setPublication(bookRequest.getPublication());
            }
            if (bookRequest.getPrice() != null) {
                existingBook.setPrice(bookRequest.getPrice());
            }
            if (bookRequest.getAvailability() != null) {
                existingBook.setAvailability(bookRequest.getAvailability());
            }
            bookRepository.save(existingBook);
            log.info("Book with the {} is updated", name);
            return mapToDTO(existingBook);
        }
        throw new BookWithNameNotFoundException("Book with name is not found");
    }

    private BookResponse mapToDTO(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor())
                .publication(book.getPublication())
                .price(book.getPrice())
                .availability(book.getAvailability())
                .build();
    }

    public String deleteByName(String name
    ) {
        Optional<Book> optionalBook = bookRepository.findByName(name);
        if (optionalBook.isPresent()) {
            bookRepository.deleteByName(name);
            log.info("Book with {} deleted!!", name);
            return "Book is Deleted Successfully!!";
        }
        return String.format("Book " + name + " is not present");
    }

    public String deleteAll() {
        if (bookRepository.count() > 1) {
            bookRepository.deleteAll();
            log.info("ALl Books Deleted!!");
            return "All Books Deleted!!";
        } else
            return "No Books Found!!";
    }


}
