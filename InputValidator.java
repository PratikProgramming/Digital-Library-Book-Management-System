package com.library.management.service;

import com.library.management.exception.ValidationException;
import com.library.management.model.Book;
import java.util.List;

public class InputValidator {
    
    public static void validateNonEmpty(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " cannot be empty");
        }
    }

    public static void validateBookIdUnique(String id, List<Book> books) throws ValidationException {
        if (books.stream().anyMatch(book -> book.getId().equals(id))) {
            throw new ValidationException("Book ID already exists");
        }
    }

    public static String validateAvailability(String availability) throws ValidationException {
        String normalized = availability.trim().toUpperCase();
        if (!normalized.equals("AVAILABLE") && !normalized.equals("CHECKED OUT")) {
            throw new ValidationException("Invalid availability status");
        }
        return normalized.charAt(0) + normalized.substring(1).toLowerCase();
    }
}