package com.inventorysystem.book_service.exception;

public class BookWithNameNotFoundException extends RuntimeException {
    public BookWithNameNotFoundException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
