package com.library.services;

import com.library.models.LibraryItem;
import com.library.models.Member;
import com.library.models.BorrowRecord;
import java.util.*;

public class ReportService {
    private ItemService itemService;
    private MemberService memberService;
    private BorrowService borrowService;
    
    public ReportService(ItemService itemService, MemberService memberService, BorrowService borrowService) {
        this.itemService = itemService;
        this.memberService = memberService;
        this.borrowService = borrowService;
    }
    
    public Map<LibraryItem, Integer> getTopBorrowedItems(int limit) {
        // Simple implementation - count borrow records per item
        Map<String, Integer> itemCount = new HashMap<>();
        Map<String, LibraryItem> itemMap = new HashMap<>();
        
        // Build item map
        for (LibraryItem item : itemService.getAllItems()) {
            itemMap.put(item.getId(), item);
        }
        
        // Count borrows
        for (BorrowRecord record : borrowService.getAllRecords()) {
            String itemId = record.getItemId();
            itemCount.put(itemId, itemCount.getOrDefault(itemId, 0) + 1);
        }
        
        // Sort by count
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(itemCount.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Return top items
        Map<LibraryItem, Integer> result = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sorted) {
            if (count >= limit) break;
            LibraryItem item = itemMap.get(entry.getKey());
            if (item != null) {
                result.put(item, entry.getValue());
                count++;
            }
        }
        
        return result;
    }
    
    public Map<Member, Integer> getTopActiveMembers(int limit) {
        // Count borrow records per member
        Map<String, Integer> memberCount = new HashMap<>();
        Map<String, Member> memberMap = new HashMap<>();
        
        // Build member map
        for (Member member : memberService.getAllMembers()) {
            memberMap.put(member.getId(), member);
        }
        
        // Count borrows
        for (BorrowRecord record : borrowService.getAllRecords()) {
            String memberId = record.getMemberId();
            memberCount.put(memberId, memberCount.getOrDefault(memberId, 0) + 1);
        }
        
        // Sort by count
        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(memberCount.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        // Return top members
        Map<Member, Integer> result = new LinkedHashMap<>();
        int count = 0;
        for (Map.Entry<String, Integer> entry : sorted) {
            if (count >= limit) break;
            Member member = memberMap.get(entry.getKey());
            if (member != null) {
                result.put(member, entry.getValue());
                count++;
            }
        }
        
        return result;
    }
    
    public List<BorrowRecord> getOverdueReport() {
        return borrowService.getOverdueRecords();
    }
    
    public Map<String, Integer> getItemTypeStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Books", itemService.getAllBooks().size());
        stats.put("Magazines", itemService.getAllMagazines().size());
        stats.put("Total Items", itemService.getAllItems().size());
        stats.put("Available Items", (int) itemService.getAllItems().stream()
                .filter(LibraryItem::isAvailable).count());
        return stats;
    }
    
    public Map<String, Integer> getMemberStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        List<Member> allMembers = memberService.getAllMembers();
        stats.put("Total Members", allMembers.size());
        stats.put("Active Members", memberService.getActiveMembers().size());
        stats.put("Currently Borrowing", borrowService.getActiveBorrows().size());
        stats.put("Overdue Items", borrowService.getOverdueRecords().size());
        return stats;
    }
}
