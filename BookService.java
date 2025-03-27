package com.library.management.service;

import com.library.management.exception.ValidationException;
import com.library.management.model.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BookService {
    private final List<Book> books = new ArrayList<>();

    
    public void addBook(Book book) throws ValidationException {
        validateBookForAdd(book);
        books.add(book);
    }

   
    public List<Book> getAllBooks() {
        return Collections.unmodifiableList(books);
    }

    /**
     * Finds a book by exact ID match
     * @param id Book ID to search
     * @return Optional containing found book or empty
     */
    public Optional<Book> findBookById(String id) {
        return books.stream()
                   .filter(book -> book.getId().equals(id))
                   .findFirst();
    }

    /**
     * Searches books by title containing search term (case-insensitive)
     * @param title Title search term
     * @return List of matching books
     */
    public List<Book> searchByTitle(String title) {
        String searchTerm = title.toLowerCase();
        return books.stream()
                   .filter(book -> book.getTitle().toLowerCase().contains(searchTerm))
                   .collect(Collectors.toList());
    }

    /**
     * Updates book details
     * @param id Book ID to update
     * @param updates Book object containing new values
     * @throws ValidationException If validation fails
     */
    public void updateBook(String id, Book updates) throws ValidationException {
        Book bookToUpdate = findBookById(id)
            .orElseThrow(() -> new ValidationException("Book not found"));
        
        applyUpdates(bookToUpdate, updates);
    }

    /**
     * Deletes a book by ID
     * @param id Book ID to delete
     * @return true if book was found and removed
     */
    public boolean deleteBook(String id) {
        return books.removeIf(book -> book.getId().equals(id));
    }

    // Validation and helper methods
    private void validateBookForAdd(Book book) throws ValidationException {
        InputValidator.validateNonEmpty(book.getId(), "Book ID");
        InputValidator.validateNonEmpty(book.getTitle(), "Title");
        InputValidator.validateNonEmpty(book.getAuthor(), "Author");
        InputValidator.validateBookIdUnique(book.getId(), books);
        validateAndNormalizeAvailability(book);
    }

    private void applyUpdates(Book target, Book source) throws ValidationException {
        if (source.getTitle() != null) {
            InputValidator.validateNonEmpty(source.getTitle(), "Title");
            target.setTitle(source.getTitle());
        }
        
        if (source.getAuthor() != null) {
            InputValidator.validateNonEmpty(source.getAuthor(), "Author");
            target.setAuthor(source.getAuthor());
        }
        
        if (source.getGenre() != null) {
            target.setGenre(source.getGenre());
        }
        
        if (source.getAvailability() != null) {
            target.setAvailability(InputValidator.validateAvailability(source.getAvailability()));
        }
    }

    private void validateAndNormalizeAvailability(Book book) throws ValidationException {
        String normalized = InputValidator.validateAvailability(book.getAvailability());
        book.setAvailability(normalized);
    }
}