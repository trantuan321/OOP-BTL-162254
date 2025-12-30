package com.library.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.library.models.BorrowRecord;
import com.library.models.LibraryItem;
import com.library.models.Member;

public class BorrowService {
    private List<BorrowRecord> records;
    private ItemService itemService;
    private MemberService memberService;
    private static final String FILE_NAME = "data/borrow_records.txt";
    
    public BorrowService(ItemService itemService, MemberService memberService) {
        this.itemService = itemService;
        this.memberService = memberService;
        this.records = new ArrayList<>();
        loadFromFile();
        printDebugInfo();
    }
    
    private void printDebugInfo() {
        System.out.println("\n=== BORROW SERVICE DEBUG INFO ===");
        System.out.println("Total records: " + records.size());
        
        int activeCount = 0;
        int overdueCount = 0;
        int returnedCount = 0;
        
        for (BorrowRecord record : records) {
            if (record.isReturned()) {
                returnedCount++;
            } else {
                activeCount++;
                if (record.isOverdue()) {
                    overdueCount++;
                    System.out.println("OVERDUE: " + record.getRecordId() + 
                                     " - Due: " + record.getDueDate() + 
                                     " - Days overdue: " + record.getDaysOverdue());
                }
            }
        }
        
        System.out.println("Active borrows: " + activeCount);
        System.out.println("Overdue borrows: " + overdueCount);
        System.out.println("Returned borrows: " + returnedCount);
        System.out.println("Today's date: " + LocalDate.now());
        System.out.println("================================\n");
    }
    
    public BorrowRecord borrowItem(String memberId, String itemId, int borrowDays) {
        // Validate member
        Member member = memberService.findMemberById(memberId);
        if (member == null || !member.canBorrow()) {
            System.err.println("Member validation failed for: " + memberId);
            return null;
        }
        
        // Validate item
        LibraryItem item = itemService.findItemById(itemId);
        if (item == null || !item.isAvailable()) {
            System.err.println("Item validation failed for: " + itemId);
            return null;
        }
        
        // Create borrow record
        String recordId = "BR" + String.format("%03d", records.size() + 1);
        BorrowRecord record = new BorrowRecord(recordId, memberId, itemId, borrowDays);
        
        // Update item quantity
        item.borrowItem();
        itemService.updateItem(itemId, item);
        
        // Update member's current borrows
        member.borrowItem(itemId);
        memberService.updateMember(memberId, member);
        
        // Save record
        records.add(record);
        saveToFile();
        
        return record;
    }
    
    public boolean returnItem(String recordId) {
        for (BorrowRecord record : records) {
            if (record.getRecordId().equals(recordId) && !record.isReturned()) {
                // Mark as returned
                record.markReturned();
                
                // Update item quantity
                LibraryItem item = itemService.findItemById(record.getItemId());
                if (item != null) {
                    item.returnItem();
                    itemService.updateItem(item.getId(), item);
                }
                
                // Update member's current borrows
                Member member = memberService.findMemberById(record.getMemberId());
                if (member != null) {
                    member.returnItem(record.getItemId());
                    memberService.updateMember(member.getId(), member);
                }
                
                saveToFile();
                return true;
            }
        }
        return false;
    }
    
    public List<BorrowRecord> getOverdueRecords() {
        List<BorrowRecord> overdue = new ArrayList<>();
        for (BorrowRecord record : records) {
            if (record.isOverdue() && !record.isReturned()) {
                overdue.add(record);
                System.out.println("DEBUG getOverdueRecords: Found overdue - " + record.getRecordId());
            }
        }
        System.out.println("DEBUG getOverdueRecords: Returning " + overdue.size() + " records");
        return overdue;
    }
    
    public List<BorrowRecord> getMemberRecords(String memberId) {
        List<BorrowRecord> memberRecords = new ArrayList<>();
        for (BorrowRecord record : records) {
            if (record.getMemberId().equals(memberId)) {
                memberRecords.add(record);
            }
        }
        return memberRecords;
    }
    
    public List<BorrowRecord> getActiveBorrows() {
        List<BorrowRecord> active = new ArrayList<>();
        for (BorrowRecord record : records) {
            if (!record.isReturned()) {
                active.add(record);
            }
        }
        System.out.println("DEBUG getActiveBorrows: Returning " + active.size() + " records");
        return active;
    }
    
    public BorrowRecord findRecordById(String recordId) {
        for (BorrowRecord record : records) {
            if (record.getRecordId().equals(recordId)) {
                return record;
            }
        }
        return null;
    }
    
    public List<BorrowRecord> getAllRecords() {
        return new ArrayList<>(records);
    }
    
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (BorrowRecord record : records) {
                writer.println(String.format("%s,%s,%s,%s,%s,%s", 
                        record.getRecordId(), 
                        record.getMemberId(), 
                        record.getItemId(),
                        record.formatDate(record.getBorrowDate()), 
                        record.formatDate(record.getDueDate()), 
                        record.formatDate(record.getReturnDate())));
            }
        } catch (IOException e) {
            System.err.println("Error saving borrow records: " + e.getMessage());
        }
    }
    
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.err.println("Borrow records file not found: " + FILE_NAME);
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 6) {
                        String recordId = parts[0].trim();
                        String memberId = parts[1].trim();
                        String itemId = parts[2].trim();
                        
                        LocalDate borrowDate = BorrowRecord.parseDate(parts[3]);
                        LocalDate dueDate = BorrowRecord.parseDate(parts[4]);
                        LocalDate returnDate = BorrowRecord.parseDate(parts[5]);
                        
                        BorrowRecord record = new BorrowRecord(recordId, memberId, itemId, 
                                                              borrowDate, dueDate, returnDate);
                        
                        records.add(record);
                    } else {
                        System.err.println("Invalid line format at line " + lineCount + ": " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineCount + ": " + e.getMessage());
                }
            }
            System.out.println("Loaded " + records.size() + " borrow records from file");
        } catch (IOException e) {
            System.err.println("Error loading borrow records: " + e.getMessage());
        }
    }
}
