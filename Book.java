package com.library.management.model;


public class Book {
    private final String id;
    private String title;
    private String author;
    private String genre;
    private String availability;

    public Book(String id, String title, String author, String genre, String availability) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availability = availability;
    }

    // Getter methods
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getAvailability() { return availability; }

    // Setter methods (except for immutable id)
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setAvailability(String availability) { this.availability = availability; }

    
    public void printDetails() {
        System.out.printf("%-15s: %s\n", "ID", id);
        System.out.printf("%-15s: %s\n", "Title", title);
        System.out.printf("%-15s: %s\n", "Author", author);
        System.out.printf("%-15s: %s\n", "Genre", genre);
        System.out.printf("%-15s: %s\n", "Availability", availability);
        System.out.println("-".repeat(30));
    }

    
    @Override
    public String toString() {
        return String.format(
            "Book[id=%s, title=%s, author=%s, genre=%s, availability=%s]",
            id, title, author, genre, availability
        );
    }
}