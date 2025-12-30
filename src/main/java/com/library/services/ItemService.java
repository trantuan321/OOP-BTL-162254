package com.library.services;

import com.library.models.*;
import java.util.*;
import java.io.*;

public class ItemService {
    private List<LibraryItem> items;
    private static final String FILE_NAME = "data/items.txt";
    
    public ItemService() {
        items = new ArrayList<>();
        loadFromFile();
        System.out.println("ItemService initialized. Total items: " + items.size());
    }
    
    public boolean addItem(LibraryItem item) {
        System.out.println("DEBUG: Adding item - ID: " + item.getId() + ", Title: " + item.getTitle());
        
        // Check if ID already exists
        for (LibraryItem i : items) {
            if (i.getId().equals(item.getId())) {
                System.out.println("ERROR: Item ID already exists: " + item.getId());
                return false;
            }
        }
        
        items.add(item);
        boolean saved = saveToFile();
        System.out.println("DEBUG: Item added successfully: " + saved);
        return saved;
    }
    
    public boolean updateItem(String itemId, LibraryItem updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(itemId)) {
                items.set(i, updatedItem);
                return saveToFile();
            }
        }
        return false;
    }
    
    public boolean deleteItem(String itemId) {
        System.out.println("DEBUG: Deleting item with ID: " + itemId);
        Iterator<LibraryItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            LibraryItem item = iterator.next();
            if (item.getId().equals(itemId)) {
                iterator.remove();
                boolean saved = saveToFile();
                System.out.println("DEBUG: Item deleted: " + saved);
                return saved;
            }
        }
        System.out.println("DEBUG: Item not found for deletion: " + itemId);
        return false;
    }
    
    public LibraryItem findItemById(String itemId) {
        for (LibraryItem item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
    }
    
    public List<LibraryItem> searchByTitle(String title) {
        System.out.println("DEBUG: Searching by title: '" + title + "'");
        List<LibraryItem> result = new ArrayList<>();
        String searchTerm = title.toLowerCase();
        
        for (LibraryItem item : items) {
            if (item.getTitle().toLowerCase().contains(searchTerm)) {
                result.add(item);
            }
        }
        
        System.out.println("DEBUG: Found " + result.size() + " items by title");
        return result;
    }
    
    public List<Book> searchByAuthor(String author) {
        System.out.println("DEBUG: Searching by author: '" + author + "'");
        List<Book> result = new ArrayList<>();
        String searchTerm = author.toLowerCase();
        
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                Book book = (Book) item;
                if (book.getAuthor().toLowerCase().contains(searchTerm)) {
                    result.add(book);
                }
            }
        }
        
        System.out.println("DEBUG: Found " + result.size() + " books by author");
        return result;
    }
    
    public Book searchByISBN(String isbn) {
        System.out.println("DEBUG: Searching by ISBN: '" + isbn + "'");
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                Book book = (Book) item;
                if (book.getIsbn().equals(isbn)) {
                    System.out.println("DEBUG: Found book by ISBN: " + book.getTitle());
                    return book;
                }
            }
        }
        System.out.println("DEBUG: No book found with ISBN: " + isbn);
        return null;
    }
    
    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items);
    }
    
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item instanceof Book) {
                books.add((Book) item);
            }
        }
        System.out.println("DEBUG: Total books: " + books.size());
        return books;
    }
    
    public List<Magazine> getAllMagazines() {
        List<Magazine> magazines = new ArrayList<>();
        for (LibraryItem item : items) {
            if (item instanceof Magazine) {
                magazines.add((Magazine) item);
            }
        }
        System.out.println("DEBUG: Total magazines: " + magazines.size());
        return magazines;
    }
    
    public int getTotalItems() {
        return items.size();
    }
    
    private boolean saveToFile() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
                for (LibraryItem item : items) {
                    if (item instanceof Book) {
                        Book book = (Book) item;
                        writer.println(String.format("BOOK,%s,%s,%s,%s,%s,%d,%d", 
                                book.getId(), book.getTitle(), book.getAuthor(), 
                                book.getIsbn(), book.getPublisher(), 
                                book.getPublicationYear(), book.getAvailableQuantity()));
                    } else if (item instanceof Magazine) {
                        Magazine magazine = (Magazine) item;
                        writer.println(String.format("MAGAZINE,%s,%s,%d,%s,%d,%d", 
                                magazine.getId(), magazine.getTitle(), 
                                magazine.getIssueNumber(), magazine.getCategory(),
                                magazine.getPublicationYear(), magazine.getAvailableQuantity()));
                    }
                }
                System.out.println("DEBUG: Saved " + items.size() + " items to file");
                return true;
            }
        } catch (IOException e) {
            System.err.println("ERROR saving items: " + e.getMessage());
            return false;
        }
    }
    
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("DEBUG: Items file not found, starting with empty list");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int loaded = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    if (parts[0].equals("BOOK")) {
                        Book book = new Book(parts[1], parts[2], parts[3], parts[4], 
                                           parts[5], Integer.parseInt(parts[6]), 
                                           Integer.parseInt(parts[7]));
                        items.add(book);
                        loaded++;
                    } else if (parts[0].equals("MAGAZINE")) {
                        Magazine magazine = new Magazine(parts[1], parts[2], 
                                                       Integer.parseInt(parts[3]), 
                                                       parts[4], 
                                                       Integer.parseInt(parts[5]), 
                                                       Integer.parseInt(parts[6]));
                        items.add(magazine);
                        loaded++;
                    }
                }
            }
            System.out.println("DEBUG: Loaded " + loaded + " items from file");
        } catch (IOException e) {
            System.err.println("ERROR loading items: " + e.getMessage());
        }
    }
}
