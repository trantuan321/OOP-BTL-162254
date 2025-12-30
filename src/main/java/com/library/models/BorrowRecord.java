package com.library.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BorrowRecord {
    private String recordId;
    private String memberId;
    private String itemId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double lateFee;
    
    public BorrowRecord() {}
    
    public BorrowRecord(String recordId, String memberId, String itemId) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(14); // Default 14 days
        this.lateFee = 0.0;
    }
    
    public BorrowRecord(String recordId, String memberId, String itemId, int borrowDays) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(borrowDays);
        this.lateFee = 0.0;
    }
    
    public BorrowRecord(String recordId, String memberId, String itemId, 
                       LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate) {
        this.recordId = recordId;
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.lateFee = 0.0;
    }
    
    public boolean isOverdue() {
        if (isReturned()) {
            // If returned, check if returned after due date
            return returnDate != null && dueDate != null && returnDate.isAfter(dueDate);
        } else {
            // If not returned, check if today is after due date
            return dueDate != null && LocalDate.now().isAfter(dueDate);
        }
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        
        if (isReturned()) {
            return ChronoUnit.DAYS.between(dueDate, returnDate);
        } else {
            return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
    }
    
    public void markReturned() {
        this.returnDate = LocalDate.now();
        if (isOverdue()) {
            this.lateFee = getDaysOverdue() * 1.0; // $1 per day
        }
    }
    
    public boolean isReturned() {
        return returnDate != null;
    }
    
    // Parse date from string (for loading from file)
    public static LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateString);
            return null;
        }
    }
    
    // Format date for display
    public String formatDate(LocalDate date) {
        if (date == null) return "N/A";
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    // Getters
    public String getRecordId() { return recordId; }
    public String getMemberId() { return memberId; }
    public String getItemId() { return itemId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getLateFee() { return lateFee; }
    
    // Setters
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setLateFee(double lateFee) { this.lateFee = lateFee; }
    
    @Override
    public String toString() {
        return String.format("BorrowRecord [ID: %s, Member: %s, Item: %s, Borrowed: %s, Due: %s, Returned: %s, Overdue: %s]", 
                recordId, memberId, itemId, 
                formatDate(borrowDate), 
                formatDate(dueDate),
                formatDate(returnDate),
                isOverdue() ? "Yes (" + getDaysOverdue() + " days)" : "No");
    }
}
