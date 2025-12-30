package com.library.ui;

import com.library.services.*;
import com.library.models.*;
import com.library.config.Config;
import java.util.*;
import java.util.Map;

public class ConsoleUI {
    private Scanner scanner;
    private ItemService itemService;
    private MemberService memberService;
    private BorrowService borrowService;
    private ReportService reportService;
    
    public ConsoleUI() {
        scanner = new Scanner(System.in);
        itemService = new ItemService();
        memberService = new MemberService();
        borrowService = new BorrowService(itemService, memberService);
        reportService = new ReportService(itemService, memberService, borrowService);
    }
    
    public void start() {
        Config.printWelcome();
        System.out.println("Running in CONSOLE mode");
        System.out.println("Run without --console argument for GUI mode\n");
        mainMenu();
    }
    
    private void mainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Manage Items");
            System.out.println("2. Manage Members");
            System.out.println("3. Borrow/Return Items");
            System.out.println("4. View Reports");
            System.out.println("5. Switch to GUI Mode");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    itemManagementMenu();
                    break;
                case "2":
                    memberManagementMenu();
                    break;
                case "3":
                    borrowReturnMenu();
                    break;
                case "4":
                    reportsMenu();
                    break;
                case "5":
                    System.out.println("Please restart the application without --console argument for GUI mode.");
                    return;
                case "6":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void itemManagementMenu() {
        while (true) {
            System.out.println("\n=== ITEM MANAGEMENT ===");
            System.out.println("1. View All Items");
            System.out.println("2. Add New Item");
            System.out.println("3. Search Items");
            System.out.println("4. Update Item");
            System.out.println("5. Delete Item");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    viewAllItems();
                    break;
                case "2":
                    addNewItem();
                    break;
                case "3":
                    searchItems();
                    break;
                case "4":
                    updateItem();
                    break;
                case "5":
                    deleteItem();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private void viewAllItems() {
        System.out.println("\n=== ALL ITEMS ===");
        List<LibraryItem> items = itemService.getAllItems();
        if (items.isEmpty()) {
            System.out.println("No items found.");
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i + 1) + ". " + items.get(i));
            }
        }
    }
    
    private void addNewItem() {
        System.out.println("\n=== ADD NEW ITEM ===");
        System.out.print("Item Type (1=Book, 2=Magazine): ");
        String typeChoice = scanner.nextLine();
        
        System.out.print("Item ID (B001 for books, MG001 for magazines): ");
        String id = scanner.nextLine();
        
        System.out.print("Title: ");
        String title = scanner.nextLine();
        
        System.out.print("Publication Year: ");
        int year = Integer.parseInt(scanner.nextLine());
        
        System.out.print("Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        
        if (typeChoice.equals("1")) {
            System.out.print("Author: ");
            String author = scanner.nextLine();
            
            System.out.print("ISBN: ");
            String isbn = scanner.nextLine();
            
            System.out.print("Publisher: ");
            String publisher = scanner.nextLine();
            
            Book book = new Book(id, title, author, isbn, publisher, year, quantity);
            if (itemService.addItem(book)) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book. ID might already exist.");
            }
        } else if (typeChoice.equals("2")) {
            System.out.print("Issue Number: ");
            int issue = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Category: ");
            String category = scanner.nextLine();
            
            Magazine magazine = new Magazine(id, title, issue, category, year, quantity);
            if (itemService.addItem(magazine)) {
                System.out.println("Magazine added successfully!");
            } else {
                System.out.println("Failed to add magazine.");
            }
        }
    }
    
    private void searchItems() {
        System.out.println("\n=== SEARCH ITEMS ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author (Books only)");
        System.out.println("3. Search by ISBN (Books only)");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1":
                System.out.print("Enter title to search: ");
                String title = scanner.nextLine();
                List<LibraryItem> results = itemService.searchByTitle(title);
                displaySearchResults(results);
                break;
            case "2":
                System.out.print("Enter author to search: ");
                String author = scanner.nextLine();
                List<Book> books = itemService.searchByAuthor(author);
                displayBookResults(books);
                break;
            case "3":
                System.out.print("Enter ISBN to search: ");
                String isbn = scanner.nextLine();
                Book book = itemService.searchByISBN(isbn);
                if (book != null) {
                    System.out.println("Found: " + book);
                } else {
                    System.out.println("No book found with ISBN: " + isbn);
                }
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
    
    private void displaySearchResults(List<LibraryItem> items) {
        if (items.isEmpty()) {
            System.out.println("No items found.");
        } else {
            System.out.println("\nSearch Results (" + items.size() + " items):");
            for (LibraryItem item : items) {
                System.out.println("- " + item);
            }
        }
    }
    
    private void displayBookResults(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\nSearch Results (" + books.size() + " books):");
            for (Book book : books) {
                System.out.println("- " + book);
            }
        }
    }
    
    private void updateItem() {
        System.out.print("Enter Item ID to update: ");
        String id = scanner.nextLine();
        
        LibraryItem item = itemService.findItemById(id);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.println("Current item: " + item);
        System.out.print("New title (press Enter to keep current): ");
        String newTitle = scanner.nextLine();
        if (!newTitle.isEmpty()) {
            item.setTitle(newTitle);
        }
        
        System.out.print("New quantity: ");
        String newQtyStr = scanner.nextLine();
        if (!newQtyStr.isEmpty()) {
            item.setAvailableQuantity(Integer.parseInt(newQtyStr));
        }
        
        if (itemService.updateItem(id, item)) {
            System.out.println("Item updated successfully!");
        } else {
            System.out.println("Failed to update item.");
        }
    }
    
    private void deleteItem() {
        System.out.print("Enter Item ID to delete: ");
        String id = scanner.nextLine();
        
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (itemService.deleteItem(id)) {
                System.out.println("Item deleted successfully!");
            } else {
                System.out.println("Failed to delete item.");
            }
        }
    }
    
    private void memberManagementMenu() {
        while (true) {
            System.out.println("\n=== MEMBER MANAGEMENT ===");
            System.out.println("1. View All Members");
            System.out.println("2. Add New Member");
            System.out.println("3. Search Members");
            System.out.println("4. Update Member");
            System.out.println("5. Delete Member");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    viewAllMembers();
                    break;
                case "2":
                    addNewMember();
                    break;
                case "3":
                    searchMembers();
                    break;
                case "4":
                    updateMember();
                    break;
                case "5":
                    deleteMember();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private void viewAllMembers() {
        System.out.println("\n=== ALL MEMBERS ===");
        List<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            for (Member member : members) {
                System.out.println("- " + member);
            }
        }
    }
    
    private void addNewMember() {
        System.out.println("\n=== ADD NEW MEMBER ===");
        System.out.print("Member ID (M001, M002, etc.): ");
        String id = scanner.nextLine();
        
        System.out.print("Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        
        Member member = new Member(id, name, email, phone);
        if (memberService.addMember(member)) {
            System.out.println("Member added successfully!");
        } else {
            System.out.println("Failed to add member. ID might already exist.");
        }
    }
    
    private void searchMembers() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        
        List<Member> results = memberService.searchByName(name);
        if (results.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println("\nSearch Results (" + results.size() + " members):");
            for (Member member : results) {
                System.out.println("- " + member);
            }
        }
    }
    
    private void updateMember() {
        System.out.print("Enter Member ID to update: ");
        String id = scanner.nextLine();
        
        Member member = memberService.findMemberById(id);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }
        
        System.out.println("Current member: " + member);
        System.out.print("New name (press Enter to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            member.setName(newName);
        }
        
        System.out.print("New email: ");
        String newEmail = scanner.nextLine();
        if (!newEmail.isEmpty()) {
            member.setEmail(newEmail);
        }
        
        System.out.print("New phone: ");
        String newPhone = scanner.nextLine();
        if (!newPhone.isEmpty()) {
            member.setPhone(newPhone);
        }
        
        if (memberService.updateMember(id, member)) {
            System.out.println("Member updated successfully!");
        } else {
            System.out.println("Failed to update member.");
        }
    }
    
    private void deleteMember() {
        System.out.print("Enter Member ID to delete: ");
        String id = scanner.nextLine();
        
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("yes")) {
            if (memberService.deleteMember(id)) {
                System.out.println("Member deleted successfully!");
            } else {
                System.out.println("Failed to delete member.");
            }
        }
    }
    
    private void borrowReturnMenu() {
        while (true) {
            System.out.println("\n=== BORROW/RETURN ===");
            System.out.println("1. Borrow Item");
            System.out.println("2. Return Item");
            System.out.println("3. View Active Borrows");
            System.out.println("4. View Overdue Items");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    borrowItem();
                    break;
                case "2":
                    returnItem();
                    break;
                case "3":
                    viewActiveBorrows();
                    break;
                case "4":
                    viewOverdueItems();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private void borrowItem() {
        System.out.println("\n=== BORROW ITEM ===");
        System.out.print("Member ID: ");
        String memberId = scanner.nextLine();
        
        System.out.print("Item ID: ");
        String itemId = scanner.nextLine();
        
        System.out.print("Borrow days (default 14): ");
        String daysStr = scanner.nextLine();
        int days = daysStr.isEmpty() ? 14 : Integer.parseInt(daysStr);
        
        BorrowRecord record = borrowService.borrowItem(memberId, itemId, days);
        if (record != null) {
            System.out.println("Item borrowed successfully!");
            System.out.println("Borrow Record: " + record.getRecordId());
            System.out.println("Due Date: " + record.getDueDate());
        } else {
            System.out.println("Failed to borrow item. Check member status and item availability.");
        }
    }
    
    private void returnItem() {
        System.out.println("\n=== RETURN ITEM ===");
        System.out.print("Borrow Record ID: ");
        String recordId = scanner.nextLine();
        
        if (borrowService.returnItem(recordId)) {
            System.out.println("Item returned successfully!");
        } else {
            System.out.println("Failed to return item.");
        }
    }
    
    private void viewActiveBorrows() {
        System.out.println("\n=== ACTIVE BORROWS ===");
        List<BorrowRecord> active = borrowService.getActiveBorrows();
        if (active.isEmpty()) {
            System.out.println("No active borrows.");
        } else {
            for (BorrowRecord record : active) {
                System.out.println("- " + record);
            }
        }
    }
    
    private void viewOverdueItems() {
        System.out.println("\n=== OVERDUE ITEMS ===");
        List<BorrowRecord> overdue = borrowService.getOverdueRecords();
        if (overdue.isEmpty()) {
            System.out.println("No overdue items.");
        } else {
            for (BorrowRecord record : overdue) {
                System.out.println("- " + record);
            }
        }
    }
    
    private void reportsMenu() {
        while (true) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Top Borrowed Items");
            System.out.println("2. Top Active Members");
            System.out.println("3. Overdue Report");
            System.out.println("4. Library Statistics");
            System.out.println("5. Member Statistics");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose option: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    showTopBorrowedItems();
                    break;
                case "2":
                    showTopActiveMembers();
                    break;
                case "3":
                    showOverdueReport();
                    break;
                case "4":
                    showItemStatistics();
                    break;
                case "5":
                    showMemberStatistics();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
    private void showTopBorrowedItems() {
        System.out.print("How many top items to show? ");
        int limit = Integer.parseInt(scanner.nextLine());
        
        Map<LibraryItem, Integer> topItems = reportService.getTopBorrowedItems(limit);
        if (topItems.isEmpty()) {
            System.out.println("No borrow records found.");
        } else {
            System.out.println("\n=== TOP " + limit + " BORROWED ITEMS ===");
            int rank = 1;
            for (Map.Entry<LibraryItem, Integer> entry : topItems.entrySet()) {
                System.out.println(rank + ". " + entry.getKey().getTitle() + 
                                 " - Borrowed " + entry.getValue() + " times");
                rank++;
            }
        }
    }
    
    private void showTopActiveMembers() {
        System.out.print("How many top members to show? ");
        int limit = Integer.parseInt(scanner.nextLine());
        
        Map<Member, Integer> topMembers = reportService.getTopActiveMembers(limit);
        if (topMembers.isEmpty()) {
            System.out.println("No member records found.");
        } else {
            System.out.println("\n=== TOP " + limit + " ACTIVE MEMBERS ===");
            int rank = 1;
            for (Map.Entry<Member, Integer> entry : topMembers.entrySet()) {
                System.out.println(rank + ". " + entry.getKey().getName() + 
                                 " - Borrowed " + entry.getValue() + " items");
                rank++;
            }
        }
    }
    
    private void showOverdueReport() {
        List<BorrowRecord> overdue = reportService.getOverdueReport();
        if (overdue.isEmpty()) {
            System.out.println("No overdue items.");
        } else {
            System.out.println("\n=== OVERDUE REPORT ===");
            System.out.println("Total overdue items: " + overdue.size());
            for (BorrowRecord record : overdue) {
                System.out.println("- " + record);
            }
        }
    }
    
    private void showItemStatistics() {
        Map<String, Integer> stats = reportService.getItemTypeStatistics();
        System.out.println("\n=== LIBRARY STATISTICS ===");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    
    private void showMemberStatistics() {
        Map<String, Integer> stats = reportService.getMemberStatistics();
        System.out.println("\n=== MEMBER STATISTICS ===");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
