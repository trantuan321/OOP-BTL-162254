package com.library.ui.dialogs;

import com.library.models.*;
import com.library.services.ItemService;
import com.library.ui.components.CustomButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AddItemDialog extends JDialog {
    private JTextField idField, titleField, authorField, isbnField, publisherField;
    private JTextField yearField, quantityField, issueField, categoryField;
    private JComboBox<String> typeCombo;
    private JPanel bookPanel, magazinePanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    
    private ItemService itemService;
    private boolean success = false;
    private LibraryItem editingItem;
    
    public AddItemDialog(JFrame parent) {
        this(parent, null);
    }
    
    public AddItemDialog(JFrame parent, LibraryItem itemToEdit) {
        super(parent, itemToEdit == null ? "Add New Item" : "Edit Item", true);
        this.editingItem = itemToEdit;
        this.itemService = new ItemService();
        initialize();
        if (itemToEdit != null) {
            populateFields(itemToEdit);
        }
    }
    
    private void initialize() {
        setSize(500, 550);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        
        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Item type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Item Type:"));
        typeCombo = new JComboBox<>(new String[]{"Book", "Magazine"});
        typeCombo.addActionListener(this::typeChanged);
        typePanel.add(typeCombo);
        formPanel.add(typePanel);
        
        formPanel.add(Box.createVerticalStrut(15));
        
        // Card panel for book/magazine specific fields
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Book panel
        bookPanel = createBookPanel();
        cardPanel.add(bookPanel, "Book");
        
        // Magazine panel
        magazinePanel = createMagazinePanel();
        cardPanel.add(magazinePanel, "Magazine");
        
        formPanel.add(cardPanel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        CustomButton saveButton = new CustomButton("Save");
        CustomButton cancelButton = new CustomButton("Cancel");
        
        saveButton.addActionListener(this::saveItem);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Book ID (B001):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        idField = new JTextField(20);
        panel.add(idField, gbc);
        
        // Title
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        titleField = new JTextField(20);
        panel.add(titleField, gbc);
        
        // Author
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        authorField = new JTextField(20);
        panel.add(authorField, gbc);
        
        // ISBN
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        isbnField = new JTextField(20);
        panel.add(isbnField, gbc);
        
        // Publisher
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Publisher:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        publisherField = new JTextField(20);
        panel.add(publisherField, gbc);
        
        // Year
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Publication Year:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        yearField = new JTextField(10);
        panel.add(yearField, gbc);
        
        // Quantity
        gbc.gridx = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 3;
        quantityField = new JTextField(10);
        panel.add(quantityField, gbc);
        
        return panel;
    }
    
    private JPanel createMagazinePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        int row = 0;
        
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Magazine ID (MG001):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        idField = new JTextField(20);
        panel.add(idField, gbc);
        
        // Title
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        titleField = new JTextField(20);
        panel.add(titleField, gbc);
        
        // Issue Number
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Issue Number:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        issueField = new JTextField(20);
        panel.add(issueField, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        categoryField = new JTextField(20);
        panel.add(categoryField, gbc);
        
        // Year
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 1;
        panel.add(new JLabel("Publication Year:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1;
        yearField = new JTextField(10);
        panel.add(yearField, gbc);
        
        // Quantity
        gbc.gridx = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 3;
        quantityField = new JTextField(10);
        panel.add(quantityField, gbc);
        
        return panel;
    }
    
    private void typeChanged(ActionEvent e) {
        cardLayout.show(cardPanel, (String) typeCombo.getSelectedItem());
    }
    
    private void populateFields(LibraryItem item) {
        if (item instanceof Book) {
            typeCombo.setSelectedItem("Book");
            Book book = (Book) item;
            idField.setText(book.getId());
            titleField.setText(book.getTitle());
            authorField.setText(book.getAuthor());
            isbnField.setText(book.getIsbn());
            publisherField.setText(book.getPublisher());
            yearField.setText(String.valueOf(book.getPublicationYear()));
            quantityField.setText(String.valueOf(book.getAvailableQuantity()));
        } else if (item instanceof Magazine) {
            typeCombo.setSelectedItem("Magazine");
            Magazine magazine = (Magazine) item;
            idField.setText(magazine.getId());
            titleField.setText(magazine.getTitle());
            issueField.setText(String.valueOf(magazine.getIssueNumber()));
            categoryField.setText(magazine.getCategory());
            yearField.setText(String.valueOf(magazine.getPublicationYear()));
            quantityField.setText(String.valueOf(magazine.getAvailableQuantity()));
        }
    }
    
    private void saveItem(ActionEvent e) {
        try {
            String type = (String) typeCombo.getSelectedItem();
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());
            
            if (id.isEmpty() || title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            LibraryItem item;
            
            if (type.equals("Book")) {
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                String publisher = publisherField.getText().trim();
                
                if (author.isEmpty() || isbn.isEmpty() || publisher.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all book fields.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                item = new Book(id, title, author, isbn, publisher, year, quantity);
            } else {
                int issue = Integer.parseInt(issueField.getText().trim());
                String category = categoryField.getText().trim();
                
                if (category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all magazine fields.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                item = new Magazine(id, title, issue, category, year, quantity);
            }
            
            boolean result;
            if (editingItem != null) {
                result = itemService.updateItem(editingItem.getId(), item);
            } else {
                result = itemService.addItem(item);
            }
            
            if (result) {
                success = true;
                JOptionPane.showMessageDialog(this, 
                    editingItem == null ? "Item added successfully!" : "Item updated successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    editingItem == null ? "Failed to add item. ID might already exist." : "Failed to update item.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for year, quantity, and issue (if magazine).", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "An error occurred: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isSuccess() {
        return success;
    }
}
