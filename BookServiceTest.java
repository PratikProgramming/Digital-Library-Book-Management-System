package com.library.management.service;

import com.library.management.model.Book;
import com.library.management.exception.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {
    private final BookService service = new BookService();

    @Test
    void shouldAddValidBook() throws ValidationException {
        Book book = new Book("001", "Clean Code", "Robert Martin", 
                           "Programming", "Available");
        service.addBook(book);
        assertEquals(1, service.getAllBooks().size());
    }

    @Test
    void shouldRejectDuplicateId() {
        Book book1 = new Book("001", "Title 1", "Author 1", "", "Available");
        Book book2 = new Book("001", "Title 2", "Author 2", "", "Available");
        
        assertThrows(ValidationException.class, () -> {
            service.addBook(book1);
            service.addBook(book2);
        });
    }
}