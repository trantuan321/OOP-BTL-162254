package com.library.ui.panels;

import com.library.models.*;
import com.library.services.ItemService;
import com.library.ui.components.CustomTable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ItemManagementPanel extends JPanel {
    private CustomTable itemTable;
    private ItemService itemService;
    private JComboBox<String> filterCombo;
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private Color panelBgColor = Color.WHITE;
    private Color primaryColor = new Color(52, 152, 219);
    
    public ItemManagementPanel() {
        itemService = new ItemService();
        initializeUI();
        loadItems();
    }
    
    private void initializeUI() {
    setLayout(new BorderLayout(10, 10));
    setBackground(panelBgColor);
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Top
    JPanel topPanel = createTopPanel();
    add(topPanel, BorderLayout.NORTH);

    // ===== CONTENT PANEL =====
    JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
    contentPanel.setBackground(panelBgColor);

    JPanel searchPanel = createSearchPanel();
    JPanel tablePanel = createTablePanel();

    contentPanel.add(searchPanel, BorderLayout.NORTH);
    contentPanel.add(tablePanel, BorderLayout.CENTER);

    add(contentPanel, BorderLayout.CENTER);
}

    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelBgColor);
        
        // Back to Dashboard button - SỬ DỤNG JBUTTON THƯỜNG
        JButton backButton = new JButton("← Back to Dashboard");
        styleButton(backButton, new Color(52, 152, 219));
        backButton.addActionListener(e -> {
            System.out.println("Back to Dashboard clicked");
            navigateToDashboard();
        });
        
        // Title
        JLabel titleLabel = new JLabel("📚 Item Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Action buttons - SỬ DỤNG JBUTTON THƯỜNG
        JPanel actionPanel = createActionPanel();
        topPanel.add(actionPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(panelBgColor);
        
        // SỬ DỤNG JBUTTON THƯỜNG VỚI ACTIONLISTENER RÕ RÀNG
        JButton addBookButton = new JButton("📕 Add New Book");
        JButton addMagazineButton = new JButton("📗 Add Magazine");
        JButton editButton = new JButton("✎ Edit");
        JButton deleteButton = new JButton("🗑 Delete");
        JButton refreshButton = new JButton("↻ Refresh");
        
        // Style các nút
        styleButton(addBookButton, new Color(46, 204, 113)); // Green
        styleButton(addMagazineButton, new Color(155, 89, 182)); // Purple
        styleButton(editButton, new Color(52, 152, 219)); // Blue
        styleButton(deleteButton, new Color(231, 76, 60)); // Red
        styleButton(refreshButton, new Color(149, 165, 166)); // Gray
        
        // Thêm ActionListener với debug
        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DEBUG: Add Book button clicked!");
                showAddBookDialog();
            }
        });
        
        addMagazineButton.addActionListener(e -> {
            System.out.println("DEBUG: Add Magazine button clicked!");
            showAddMagazineDialog();
        });
        
        editButton.addActionListener(e -> {
            System.out.println("DEBUG: Edit button clicked!");
            editItem();
        });
        
        deleteButton.addActionListener(e -> {
            System.out.println("DEBUG: Delete button clicked!");
            deleteItem();
        });
        
        refreshButton.addActionListener(e -> {
            System.out.println("DEBUG: Refresh button clicked!");
            loadItems();
        });
        
        actionPanel.add(addBookButton);
        actionPanel.add(addMagazineButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(refreshButton);
        
        return actionPanel;
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "🔍 Advanced Search",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(100, 100, 100)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Filter section
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(panelBgColor);
        filterPanel.add(new JLabel("Filter by type:"));
        
        filterCombo = new JComboBox<>(new String[]{"All Items", "Books Only", "Magazines Only"});
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterCombo.setPreferredSize(new Dimension(150, 35));
        filterPanel.add(filterCombo);
        
        // Search section với nhiều loại tìm kiếm
        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchInputPanel.setBackground(panelBgColor);
        
        searchTypeCombo = new JComboBox<>(new String[]{"Title", "Author", "ISBN", "All Fields"});
        searchTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchTypeCombo.setPreferredSize(new Dimension(120, 35));
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(250, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Thêm sự kiện Enter để search
        searchField.addActionListener(e -> performSearch());
        
        JButton searchButton = new JButton("🔍 Search");
        styleButton(searchButton, new Color(52, 152, 219));
        searchButton.addActionListener(e -> {
            System.out.println("DEBUG: Search button clicked!");
            performSearch();
        });
        
        JButton clearButton = new JButton("Clear");
        styleButton(clearButton, new Color(149, 165, 166));
        clearButton.addActionListener(e -> {
            System.out.println("DEBUG: Clear button clicked!");
            searchField.setText("");
            loadItems();
        });
        
        searchInputPanel.add(new JLabel("Search by:"));
        searchInputPanel.add(searchTypeCombo);
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);
        searchInputPanel.add(clearButton);
        
        // Add components to main panel
        panel.add(filterPanel, BorderLayout.WEST);
        panel.add(searchInputPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void performSearch() {
        String query = searchField.getText().trim();
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String filter = (String) filterCombo.getSelectedItem();
        
        System.out.println("DEBUG: Performing search - Type: " + searchType + ", Query: '" + query + "'");
        
        if (query.isEmpty()) {
            loadItems();
            return;
        }
        
        itemTable.clear();
        List<LibraryItem> items = performActualSearch(query, searchType);
        
        // Apply type filter
        int count = 0;
        for (LibraryItem item : items) {
            if (filter.equals("All Items") ||
                (filter.equals("Books Only") && item instanceof Book) ||
                (filter.equals("Magazines Only") && item instanceof Magazine)) {
                
                Object[] row = new Object[8];
                row[0] = item.getId();
                row[1] = item.getTitle();
                row[2] = item.getType();
                
                if (item instanceof Book) {
                    Book book = (Book) item;
                    row[3] = book.getAuthor();
                    row[4] = book.getIsbn();
                } else if (item instanceof Magazine) {
                    Magazine magazine = (Magazine) item;
                    row[3] = magazine.getCategory();
                    row[4] = "Issue #" + magazine.getIssueNumber();
                }
                
                row[5] = item.getPublicationYear();
                row[6] = item.getAvailableQuantity();
                row[7] = item.isAvailable() ? 
                    "<html><font color='#27ae60'>✅ Available</font></html>" : 
                    "<html><font color='#e74c3c'>❌ Unavailable</font></html>";
                
                itemTable.addRow(row);
                count++;
            }
        }
        
        System.out.println("DEBUG: Found " + count + " items");
        showStatusMessage("Found " + count + " items matching '" + query + "' (Search by: " + searchType + ")");
    }
    
    private List<LibraryItem> performActualSearch(String query, String searchType) {
        System.out.println("DEBUG: Actual search - Type: " + searchType + ", Query: " + query);
        
        switch (searchType) {
            case "Title":
                System.out.println("DEBUG: Searching by title");
                return itemService.searchByTitle(query);
                
            case "Author":
                System.out.println("DEBUG: Searching by author");
                List<Book> books = itemService.searchByAuthor(query);
                System.out.println("DEBUG: Found " + books.size() + " books by author");
                // Convert List<Book> to List<LibraryItem>
                return new java.util.ArrayList<>(books);
                
            case "ISBN":
                System.out.println("DEBUG: Searching by ISBN");
                Book book = itemService.searchByISBN(query);
                List<LibraryItem> result = new java.util.ArrayList<>();
                if (book != null) {
                    System.out.println("DEBUG: Found book by ISBN: " + book.getTitle());
                    result.add(book);
                }
                return result;
                
            case "All Fields":
            default:
                System.out.println("DEBUG: Searching all fields");
                // Search in all fields
                List<LibraryItem> allResults = new java.util.ArrayList<>();
                
                // Search by title
                List<LibraryItem> titleResults = itemService.searchByTitle(query);
                allResults.addAll(titleResults);
                System.out.println("DEBUG: Found " + titleResults.size() + " by title");
                
                // Search by author (for books)
                List<Book> authorBooks = itemService.searchByAuthor(query);
                for (Book b : authorBooks) {
                    if (!allResults.contains(b)) {
                        allResults.add(b);
                    }
                }
                System.out.println("DEBUG: Found " + authorBooks.size() + " by author");
                
                // Search by ISBN
                Book isbnBook = itemService.searchByISBN(query);
                if (isbnBook != null && !allResults.contains(isbnBook)) {
                    allResults.add(isbnBook);
                    System.out.println("DEBUG: Found 1 by ISBN");
                }
                
                System.out.println("DEBUG: Total found: " + allResults.size());
                return allResults;
        }
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelBgColor);
        
        String[] columns = {"ID", "Title", "Type", "Author/Category", "ISBN/Issue#", "Year", "Available", "Status"};
        itemTable = new CustomTable(columns);
        
        JScrollPane scrollPane = new JScrollPane(itemTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "📋 Items List",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 100, 100)
        ));
        scrollPane.setBackground(panelBgColor);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Status label
        JLabel statusLabel = new JLabel("Ready", SwingConstants.LEFT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(statusLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadItems() {
        System.out.println("DEBUG: Loading all items...");
        itemTable.clear();
        List<LibraryItem> items = itemService.getAllItems();
        System.out.println("DEBUG: Total items in service: " + items.size());
        
        for (LibraryItem item : items) {
            Object[] row = new Object[8];
            row[0] = item.getId();
            row[1] = item.getTitle();
            row[2] = item.getType();
            
            if (item instanceof Book) {
                Book book = (Book) item;
                row[3] = book.getAuthor();
                row[4] = book.getIsbn();
            } else if (item instanceof Magazine) {
                Magazine magazine = (Magazine) item;
                row[3] = magazine.getCategory();
                row[4] = "Issue #" + magazine.getIssueNumber();
            }
            
            row[5] = item.getPublicationYear();
            row[6] = item.getAvailableQuantity();
            row[7] = item.isAvailable() ? 
                "<html><font color='#27ae60'>✅ Available</font></html>" : 
                "<html><font color='#e74c3c'>❌ Unavailable</font></html>";
            
            itemTable.addRow(row);
        }
        
        System.out.println("DEBUG: Loaded " + items.size() + " items into table");
        showStatusMessage("Loaded " + items.size() + " items");
    }
    
    private void showAddBookDialog() {
        System.out.println("DEBUG: Opening Add Book dialog...");
        
        // Sử dụng JOptionPane đơn giản để test
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField quantityField = new JTextField();
        
        // Auto-generate ID
        int nextId = itemService.getAllBooks().size() + 1;
        idField.setText("B" + String.format("%03d", nextId));
        quantityField.setText("1");
        yearField.setText("2024");
        
        panel.add(new JLabel("Book ID:"));
        panel.add(idField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Add New Book", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String yearStr = yearField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            
            if (!id.isEmpty() && !title.isEmpty() && !author.isEmpty() && !isbn.isEmpty()) {
                try {
                    int year = Integer.parseInt(yearStr);
                    int quantity = Integer.parseInt(quantityStr);
                    
                    Book book = new Book(id, title, author, isbn, "Publisher", year, quantity);
                    
                    if (itemService.addItem(book)) {
                        JOptionPane.showMessageDialog(this, 
                            "✅ Book added successfully!\nID: " + id + "\nTitle: " + title,
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        loadItems();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "❌ Failed to add book. ID might already exist.",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Please enter valid numbers for year and quantity.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Please fill in all fields.",
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void showAddMagazineDialog() {
        System.out.println("DEBUG: Opening Add Magazine dialog...");
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField issueField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField quantityField = new JTextField();
        
        // Auto-generate ID
        int nextId = itemService.getAllMagazines().size() + 1;
        idField.setText("MG" + String.format("%03d", nextId));
        quantityField.setText("1");
        yearField.setText("2024");
        issueField.setText(String.valueOf(nextId));
        
        panel.add(new JLabel("Magazine ID:"));
        panel.add(idField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Issue Number:"));
        panel.add(issueField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Add New Magazine", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String issueStr = issueField.getText().trim();
            String category = categoryField.getText().trim();
            String yearStr = yearField.getText().trim();
            String quantityStr = quantityField.getText().trim();
            
            if (!id.isEmpty() && !title.isEmpty() && !issueStr.isEmpty()) {
                try {
                    int issue = Integer.parseInt(issueStr);
                    int year = Integer.parseInt(yearStr);
                    int quantity = Integer.parseInt(quantityStr);
                    
                    Magazine magazine = new Magazine(id, title, issue, category, year, quantity);
                    
                    if (itemService.addItem(magazine)) {
                        JOptionPane.showMessageDialog(this, 
                            "✅ Magazine added successfully!\nID: " + id + "\nTitle: " + title,
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                        loadItems();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "❌ Failed to add magazine. ID might already exist.",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "❌ Please enter valid numbers for issue, year and quantity.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Please fill in all fields.",
                    "Warning", 
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    
    private void editItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "❌ Please select an item to edit.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String itemId = (String) itemTable.getValueAt(selectedRow, 0);
        LibraryItem item = itemService.findItemById(itemId);
        
        if (item == null) {
            JOptionPane.showMessageDialog(this, 
                "❌ Item not found.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("DEBUG: Editing item: " + itemId);
        JOptionPane.showMessageDialog(this, 
            "Edit feature for '" + item.getTitle() + "' will be implemented soon.",
            "Coming Soon", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteItem() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "❌ Please select an item to delete.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String itemId = (String) itemTable.getValueAt(selectedRow, 0);
        String itemName = (String) itemTable.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "⚠️ Confirm Delete\n\nAre you sure you want to delete:\n" +
            "Item: " + itemName + "\n" +
            "ID: " + itemId + "\n\n" +
            "This action cannot be undone!", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("DEBUG: Deleting item: " + itemId);
            if (itemService.deleteItem(itemId)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Item '" + itemName + "' deleted successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadItems();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Failed to delete item.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showStatusMessage(String message) {
        System.out.println("STATUS: " + message);
        // Có thể hiển thị trong status bar nếu có
    }
    
    private void navigateToDashboard() {
        // Tìm MainFrame cha
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        
        if (parent instanceof JFrame) {
            JFrame frame = (JFrame) parent;
            
            // Tìm JTabbedPane trong frame
            for (Component comp : frame.getContentPane().getComponents()) {
                if (comp instanceof JTabbedPane) {
                    JTabbedPane tabbedPane = (JTabbedPane) comp;
                    tabbedPane.setSelectedIndex(0); // Switch to Dashboard tab
                    System.out.println("DEBUG: Switched to Dashboard tab");
                    break;
                }
            }
        }
    }
}
