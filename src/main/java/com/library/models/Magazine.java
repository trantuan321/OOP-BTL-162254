package com.library.models;

public class Magazine extends LibraryItem {
    private int issueNumber;
    private String category;
    
    public Magazine(String id, String title, int issueNumber, 
                   String category, int year, int quantity) {
        super(id, title, year, quantity);
        this.issueNumber = issueNumber;
        this.category = category;
    }
    
    @Override
    public String getType() {
        return "Magazine";
    }
    
    // Getters
    public int getIssueNumber() { return issueNumber; }
    public String getCategory() { return category; }
    
    // Setters
    public void setIssueNumber(int issue) { this.issueNumber = issue; }
    public void setCategory(String category) { this.category = category; }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Issue: %d, Category: %s", issueNumber, category);
    }
}
