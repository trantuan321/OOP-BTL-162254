package com.library.models;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String id;
    private String name;
    private String email;
    private String phone;
    private boolean active;
    private List<String> currentBorrows; // List of item IDs
    private String joinDate; // New field for join date
    
    public Member(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.active = true;
        this.currentBorrows = new ArrayList<>();
        this.joinDate = java.time.LocalDate.now().toString();
    }
    
    public boolean canBorrow() {
        return active && currentBorrows.size() < 5; // Max 5 items
    }
    
    public void borrowItem(String itemId) {
        if (canBorrow()) {
            currentBorrows.add(itemId);
        }
    }
    
    public boolean returnItem(String itemId) {
        return currentBorrows.remove(itemId);
    }
    
    public boolean hasBorrowedItem(String itemId) {
        return currentBorrows.contains(itemId);
    }
    
    public int getBorrowedCount() {
        return currentBorrows.size();
    }
    
    public boolean hasNoBorrowedItems() {
        return currentBorrows.isEmpty();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isActive() { return active; }
    public List<String> getCurrentBorrows() { return currentBorrows; }
    public String getJoinDate() { return joinDate; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setActive(boolean active) { this.active = active; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
    
    @Override
    public String toString() {
        return String.format("Member [ID: %s, Name: %s, Email: %s, Active: %s, Borrowed: %d, Join Date: %s]", 
                id, name, email, active ? "Yes" : "No", currentBorrows.size(), joinDate);
    }
}
