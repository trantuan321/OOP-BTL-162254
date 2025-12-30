package com.library.ui.panels;

import com.library.models.*;
import com.library.services.*;
import com.library.ui.components.CustomButton;
import com.library.ui.components.CustomTable;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BorrowReturnPanel extends JPanel {
    private CustomTable activeBorrowsTable;
    private CustomTable overdueTable;
    private BorrowService borrowService;
    private ItemService itemService;
    private MemberService memberService;
    private Color panelBgColor = Color.WHITE;
    
    public BorrowReturnPanel() {
        itemService = new ItemService();
        memberService = new MemberService();
        borrowService = new BorrowService(itemService, memberService);
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(panelBgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top panel with title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelBgColor);
        
        // Back to Dashboard button
        CustomButton backButton = new CustomButton("← Back to Dashboard");
        backButton.setBackground(new Color(52, 152, 219));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        backButton.addActionListener(e -> navigateToDashboard());
        
        // Title
        JLabel titleLabel = new JLabel("Borrow & Return Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(46, 204, 113));
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(panelBgColor);
        
        CustomButton borrowButton = new CustomButton("📖 Borrow Item");
        CustomButton returnButton = new CustomButton("↩ Return Item");
        CustomButton refreshButton = new CustomButton("↻ Refresh");
        
        borrowButton.addActionListener(e -> borrowItem());
        returnButton.addActionListener(e -> returnItem());
        refreshButton.addActionListener(e -> loadData());
        
        borrowButton.setBackground(new Color(46, 204, 113));
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        returnButton.setBackground(new Color(241, 196, 15));
        returnButton.setForeground(Color.BLACK);
        returnButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(243, 156, 18), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        refreshButton.setBackground(new Color(149, 165, 166));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        actionPanel.add(borrowButton);
        actionPanel.add(returnButton);
        actionPanel.add(refreshButton);
        
        topPanel.add(actionPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // Main content panel with tabs
        JTabbedPane contentTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        contentTabs.setBackground(panelBgColor);
        contentTabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Active borrows tab
        JPanel activeBorrowsPanel = createActiveBorrowsPanel();
        contentTabs.addTab("🔄 Active Borrows", activeBorrowsPanel);
        
        // Overdue items tab
        JPanel overduePanel = createOverduePanel();
        contentTabs.addTab("⚠️ Overdue Items", overduePanel);
        
        // History tab
        JPanel historyPanel = createHistoryPanel();
        contentTabs.addTab("📋 Borrow History", historyPanel);
        
        add(contentTabs, BorderLayout.CENTER);
    }
    
    private JPanel createActiveBorrowsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Record ID", "Member", "Item", "Borrow Date", "Due Date", "Days Left", "Status"};
        activeBorrowsTable = new CustomTable(columns);
        
        JScrollPane scrollPane = new JScrollPane(activeBorrowsTable);
        
        // FIXED: Đúng cách tạo TitledBorder
        TitledBorder activeBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "Currently Borrowed Items"
        );
        activeBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        activeBorder.setTitleColor(new Color(100, 100, 100));
        scrollPane.setBorder(activeBorder);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary
        JLabel summaryLabel = new JLabel(" ", JLabel.RIGHT);
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryLabel.setForeground(new Color(100, 100, 100));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(summaryLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createOverduePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Record ID", "Member", "Item", "Due Date", "Days Overdue", "Late Fee"};
        overdueTable = new CustomTable(columns);
        
        JScrollPane scrollPane = new JScrollPane(overdueTable);
        
        // FIXED: Đúng cách tạo TitledBorder với màu đỏ cho overdue
        TitledBorder overdueBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "Overdue Items - Action Required"
        );
        overdueBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        overdueBorder.setTitleColor(new Color(231, 76, 60));
        scrollPane.setBorder(overdueBorder);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"Record ID", "Member", "Item", "Borrow Date", "Return Date", "Status"};
        CustomTable historyTable = new CustomTable(columns);
        
        // Load all records
        List<BorrowRecord> allRecords = borrowService.getAllRecords();
        for (BorrowRecord record : allRecords) {
            Member member = memberService.findMemberById(record.getMemberId());
            LibraryItem item = itemService.findItemById(record.getItemId());
            
            if (member != null && item != null) {
                Object[] row = new Object[6];
                row[0] = record.getRecordId();
                row[1] = member.getName();
                row[2] = item.getTitle();
                row[3] = record.getBorrowDate();
                row[4] = record.isReturned() ? record.getReturnDate() : "-";
                if (record.isReturned()) {
                    row[5] = "<html><font color='#3498db'>Returned</font></html>";
                } else if (isOverdue(record)) {
                    row[5] = "<html><font color='#e74c3c'>Overdue</font></html>";
                } else {
                    row[5] = "<html><font color='#2ecc71'>Active</font></html>";
                }
                
                historyTable.addRow(row);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        TitledBorder historyBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "Complete Borrow History"
        );
        historyBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 12));
        historyBorder.setTitleColor(new Color(100, 100, 100));
        scrollPane.setBorder(historyBorder);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel summaryLabel = new JLabel("Total records: " + allRecords.size(), JLabel.RIGHT);
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryLabel.setForeground(new Color(100, 100, 100));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(summaryLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadData() {
        // Load active borrows
        activeBorrowsTable.clear();
        List<BorrowRecord> activeBorrows = borrowService.getActiveBorrows();
        
        for (BorrowRecord record : activeBorrows) {
            Member member = memberService.findMemberById(record.getMemberId());
            LibraryItem item = itemService.findItemById(record.getItemId());
            
            if (member != null && item != null) {
                LocalDate dueDate = record.getDueDate();
                LocalDate today = LocalDate.now();
                
                long daysLeft = 0;
                String status;
                
                if (dueDate != null) {
                    if (today.isBefore(dueDate)) {
                        daysLeft = ChronoUnit.DAYS.between(today, dueDate);
                        status = "<html><font color='#2ecc71'>On Time</font></html>";
                    } else if (today.isEqual(dueDate)) {
                        daysLeft = 0;
                        status = "<html><font color='#f39c12'>Due Today</font></html>";
                    } else {
                        daysLeft = ChronoUnit.DAYS.between(dueDate, today);
                        status = "<html><font color='#e74c3c'>Overdue (" + daysLeft + " days)</font></html>";
                    }
                } else {
                    status = "<html><font color='#95a5a6'>No Due Date</font></html>";
                }
                
                Object[] row = new Object[7];
                row[0] = record.getRecordId();
                row[1] = member.getName();
                row[2] = item.getTitle();
                row[3] = record.getBorrowDate();
                row[4] = dueDate != null ? dueDate.toString() : "N/A";
                row[5] = daysLeft >= 0 ? daysLeft : "Overdue";
                row[6] = status;
                
                activeBorrowsTable.addRow(row);
            }
        }
        
        // Load overdue items - FIXED LOGIC
        overdueTable.clear();
        List<BorrowRecord> overdueRecords = borrowService.getOverdueRecords();
        
        System.out.println("DEBUG: Found " + overdueRecords.size() + " overdue records");
        
        for (BorrowRecord record : overdueRecords) {
            Member member = memberService.findMemberById(record.getMemberId());
            LibraryItem item = itemService.findItemById(record.getItemId());
            
            if (member != null && item != null && !record.isReturned()) {
                LocalDate dueDate = record.getDueDate();
                LocalDate today = LocalDate.now();
                
                if (dueDate != null && today.isAfter(dueDate)) {
                    long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                    double lateFee = daysOverdue * 1.0; // $1 per day
                    
                    Object[] row = new Object[6];
                    row[0] = record.getRecordId();
                    row[1] = member.getName();
                    row[2] = item.getTitle();
                    row[3] = dueDate.toString();
                    
                    if (daysOverdue > 7) {
                        row[4] = "<html><font color='#e74c3c'><b>" + daysOverdue + " days</b></font></html>";
                    } else if (daysOverdue > 3) {
                        row[4] = "<html><font color='#f39c12'>" + daysOverdue + " days</font></html>";
                    } else {
                        row[4] = "<html><font color='#f1c40f'>" + daysOverdue + " days</font></html>";
                    }
                    
                    row[5] = String.format("$%.2f", lateFee);
                    
                    overdueTable.addRow(row);
                    
                    System.out.println("DEBUG: Added overdue record: " + record.getRecordId() + 
                                     ", Member: " + member.getName() + 
                                     ", Item: " + item.getTitle() + 
                                     ", Days overdue: " + daysOverdue);
                }
            }
        }
        
        updateStatus("Loaded " + activeBorrows.size() + " active borrows, " + 
                    overdueRecords.size() + " overdue items");
    }
    
    private boolean isOverdue(BorrowRecord record) {
        if (record.isReturned()) {
            return false;
        }
        
        LocalDate dueDate = record.getDueDate();
        if (dueDate == null) {
            return false;
        }
        
        return LocalDate.now().isAfter(dueDate);
    }
    
    private void borrowItem() {
        // Get member ID
        String memberId = JOptionPane.showInputDialog(this, 
            "<html><b>Enter Member ID</b><br>Example: M001, M002</html>", 
            "Borrow Item", JOptionPane.QUESTION_MESSAGE);
        if (memberId == null || memberId.trim().isEmpty()) return;
        
        // Get item ID
        String itemId = JOptionPane.showInputDialog(this, 
            "<html><b>Enter Item ID</b><br>Example: B001, MG001</html>", 
            "Borrow Item", JOptionPane.QUESTION_MESSAGE);
        if (itemId == null || itemId.trim().isEmpty()) return;
        
        // Get borrow days
        String daysStr = JOptionPane.showInputDialog(this, 
            "<html><b>Borrow Duration</b><br>Number of days (default: 14)</html>", "14");
        int days = 14;
        try {
            if (daysStr != null && !daysStr.trim().isEmpty()) {
                days = Integer.parseInt(daysStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "<html><b>Invalid Input</b><br>Using default 14 days.</html>", 
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
        
        // Borrow item
        BorrowRecord record = borrowService.borrowItem(memberId, itemId, days);
        if (record != null) {
            JOptionPane.showMessageDialog(this, 
                "<html><b>Success!</b><br>" +
                "Item borrowed successfully!<br>" +
                "Record ID: <b>" + record.getRecordId() + "</b><br>" +
                "Due Date: <b>" + record.getDueDate() + "</b></html>", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "<html><b>Error</b><br>" +
                "Failed to borrow item.<br>" +
                "Please check:<br>" +
                "• Member exists and is active<br>" +
                "• Item exists and is available<br>" +
                "• Member hasn't reached borrow limit</html>", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void returnItem() {
        int selectedRow = activeBorrowsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "<html><b>No Selection</b><br>Please select a borrow record to return.</html>", 
                "Information", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String recordId = (String) activeBorrowsTable.getValueAt(selectedRow, 0);
        String itemName = (String) activeBorrowsTable.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "<html><b>Confirm Return</b><br>Are you sure you want to return '<b>" + itemName + "</b>'?</html>", 
            "Confirm Return", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (borrowService.returnItem(recordId)) {
                JOptionPane.showMessageDialog(this, 
                    "<html><b>Success!</b><br>Item returned successfully.</html>", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "<html><b>Error</b><br>Failed to return item.</html>", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateStatus(String message) {
        System.out.println("Borrow/Return: " + message);
    }
    
    private void navigateToDashboard() {
        java.awt.Container parent = getParent();
        while (parent != null && !(parent instanceof com.library.ui.MainFrame)) {
            parent = parent.getParent();
        }
        
        if (parent instanceof com.library.ui.MainFrame) {
            com.library.ui.MainFrame mainFrame = (com.library.ui.MainFrame) parent;
            mainFrame.switchToTab(0);
        }
    }
}
