package com.library.models;

public class Book extends LibraryItem {
    private String author;
    private String isbn;
    private String publisher;
    
    public Book(String id, String title, String author, String isbn, 
                String publisher, int year, int quantity) {
        super(id, title, year, quantity);
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
    }
    
    @Override
    public String getType() {
        return "Book";
    }
    
    // Getters
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
    
    // Setters
    public void setAuthor(String author) { this.author = author; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Author: %s, ISBN: %s", author, isbn);
    }
}
