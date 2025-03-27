package com.library.management;

import com.library.management.exception.ValidationException;
import com.library.management.model.Book;
import com.library.management.service.BookService;
import com.library.management.service.InputValidator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main application class for library management system.
 * Handles user interactions and menu navigation.
 */
public class MainApp {
    private final BookService bookService = new BookService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new MainApp().run();
    }

    private void run() {
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            
            try {
                switch (choice) {
                    case "1" -> addBook();
                    case "2" -> viewAllBooks();
                    case "3" -> searchBooks();
                    case "4" -> updateBook();
                    case "5" -> deleteBook();
                    case "6" -> {
                        System.out.println("Exiting system...");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please enter 1-6.");
                }
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Add Book");
        System.out.println("2. View All Books");
        System.out.println("3. Search Books");
        System.out.println("4. Update Book");
        System.out.println("5. Delete Book");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addBook() throws ValidationException {
        System.out.println("\n=== Add New Book ===");
        
        String id = getInputWithValidation("Enter Book ID: ", "Book ID");
        String title = getInputWithValidation("Enter Title: ", "Title");
        String author = getInputWithValidation("Enter Author: ", "Author");
        String genre = getInput("Enter Genre: ");
        String availability = getValidAvailability();

        Book newBook = new Book(id, title, author, genre, availability);
        bookService.addBook(newBook);
        System.out.println("\nBook added successfully!");
    }

    private void viewAllBooks() {
        System.out.println("\n=== All Books ===");
        List<Book> books = bookService.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
            return;
        }
        
        books.forEach(Book::printDetails);
    }

    private void searchBooks() {
        System.out.println("\n=== Search Books ===");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Title");
        String choice = getInput("Enter your choice (1/2): ");

        switch (choice) {
            case "1" -> searchById();
            case "2" -> searchByTitle();
            default -> System.out.println("Invalid search option.");
        }
    }

    private void searchById() {
        String id = getInput("Enter Book ID: ");
        Optional<Book> book = bookService.findBookById(id);
        
        if (book.isPresent()) {
            System.out.println("\nBook found:");
            book.get().printDetails();
        } else {
            System.out.println("No book found with ID: " + id);
        }
    }

    private void searchByTitle() {
        String title = getInput("Enter Title: ");
        List<Book> results = bookService.searchByTitle(title);
        
        if (results.isEmpty()) {
            System.out.println("No books found matching: " + title);
            return;
        }
        
        System.out.println("\nFound " + results.size() + " book(s):");
        results.forEach(Book::printDetails);
    }

    private void updateBook() throws ValidationException {
        System.out.println("\n=== Update Book ===");
        String id = getInput("Enter Book ID to update: ");
        
        Optional<Book> optionalBook = bookService.findBookById(id);
        if (optionalBook.isEmpty()) {
            System.out.println("Book not found.");
            return;
        }

        Book existingBook = optionalBook.get();
        System.out.println("\nCurrent details:");
        existingBook.printDetails();

        displayUpdateMenu();
        String choice = getInput("Select field to update (1-5): ");
        
        Book updates = new Book(id, null, null, null, null);
        applyUpdates(choice, updates);
        bookService.updateBook(id, updates);
        System.out.println("Book updated successfully!");
    }

    private void displayUpdateMenu() {
        System.out.println("\nUpdate Options:");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. Genre");
        System.out.println("4. Availability");
        System.out.println("5. Cancel");
    }

    private void applyUpdates(String choice, Book updates) throws ValidationException {
        switch (choice) {
            case "1" -> updates.setTitle(getInputWithValidation("New Title: ", "Title"));
            case "2" -> updates.setAuthor(getInputWithValidation("New Author: ", "Author"));
            case "3" -> updates.setGenre(getInput("New Genre: "));
            case "4" -> updates.setAvailability(getValidAvailability());
            case "5" -> {
                System.out.println("Update cancelled.");
                return;
            }
            default -> throw new ValidationException("Invalid update choice");
        }
    }

    private void deleteBook() {
        System.out.println("\n=== Delete Book ===");
        String id = getInput("Enter Book ID to delete: ");
        
        if (bookService.deleteBook(id)) {
            System.out.println("Book deleted successfully.");
        } else {
            System.out.println("Book not found.");
        }
    }

    // Helper methods
    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String getInputWithValidation(String prompt, String fieldName) throws ValidationException {
        String input = getInput(prompt);
        InputValidator.validateNonEmpty(input, fieldName);
        return input;
    }

    private String getValidAvailability() throws ValidationException {
        while (true) {
            try {
                String availability = getInput("Availability (Available/Checked Out): ");
                return InputValidator.validateAvailability(availability);
            } catch (ValidationException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}