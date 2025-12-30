package com.library.models;

public abstract class LibraryItem {
    protected String id;
    protected String title;
    protected int publicationYear;
    protected int availableQuantity;
    
    public LibraryItem(String id, String title, int year, int quantity) {
        this.id = id;
        this.title = title;
        this.publicationYear = year;
        this.availableQuantity = quantity;
    }
    
    public abstract String getType();
    
    public boolean isAvailable() {
        return availableQuantity > 0;
    }
    
    public void borrowItem() {
        if (availableQuantity > 0) {
            availableQuantity--;
        }
    }
    
    public void returnItem() {
        availableQuantity++;
    }
    
    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getPublicationYear() { return publicationYear; }
    public int getAvailableQuantity() { return availableQuantity; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAvailableQuantity(int quantity) { this.availableQuantity = quantity; }
    
    @Override
    public String toString() {
        return String.format("%s [ID: %s, Title: %s, Year: %d, Available: %d]", 
                getType(), id, title, publicationYear, availableQuantity);
    }
}
