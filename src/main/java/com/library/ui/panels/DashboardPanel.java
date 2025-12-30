package com.library.ui.panels;

import com.library.services.*;
import com.library.ui.components.CustomButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardPanel extends JPanel {
    private JLabel totalItemsLabel;
    private JLabel totalMembersLabel;
    private JLabel activeBorrowsLabel;
    private JLabel overdueItemsLabel;
    private JLabel availableItemsLabel;
    private JLabel activeMembersLabel;
    
    private ItemService itemService;
    private MemberService memberService;
    private BorrowService borrowService;
    private ReportService reportService;
    
    public DashboardPanel() {
        initializeServices();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));
        
        // Title
        JLabel titleLabel = new JLabel("Library Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(245, 247, 250));
        
        // Statistics panel
        JPanel statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        
        // Navigation panel (replacing quick actions)
        JPanel navigationPanel = createNavigationPanel();
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Refresh stats
        refreshStatistics();
    }
    
    private void initializeServices() {
        itemService = new ItemService();
        memberService = new MemberService();
        borrowService = new BorrowService(itemService, memberService);
        reportService = new ReportService(itemService, memberService, borrowService);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            "Library Statistics",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 16),
            new Color(0, 102, 204)
        ));
        
        totalItemsLabel = createStatCard("Total Items", "0", new Color(52, 152, 219));
        totalMembersLabel = createStatCard("Total Members", "0", new Color(155, 89, 182));
        activeBorrowsLabel = createStatCard("Active Borrows", "0", new Color(46, 204, 113));
        overdueItemsLabel = createStatCard("Overdue Items", "0", new Color(231, 76, 60));
        availableItemsLabel = createStatCard("Available Items", "0", new Color(241, 196, 15));
        activeMembersLabel = createStatCard("Active Members", "0", new Color(230, 126, 34));
        
        panel.add(totalItemsLabel);
        panel.add(totalMembersLabel);
        panel.add(activeBorrowsLabel);
        panel.add(overdueItemsLabel);
        panel.add(availableItemsLabel);
        panel.add(activeMembersLabel);
        
        return panel;
    }
    
    private JLabel createStatCard(String title, String value, Color color) {
        JLabel card = new JLabel("<html><div style='text-align:center;'>" +
                "<font size='6' color='" + rgbToHex(color) + "'><b>" + value + "</b></font><br>" +
                "<font size='4' color='#666666'>" + title + "</font></div></html>");
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(25, 10, 25, 10)
        ));
        card.setHorizontalAlignment(SwingConstants.CENTER);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(245, 245, 245));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    private String rgbToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Quick Navigation",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.BOLD, 16),
            new Color(52, 152, 219)
        ));
        
        // FIXED: Create navigation buttons with visible text
        CustomButton itemsBtn = createNavigationButton("Items Management", 
            "View and manage all library items", new Color(52, 152, 219));
        itemsBtn.addActionListener(e -> navigateToItems());
        
        CustomButton membersBtn = createNavigationButton("Members Management", 
            "View and manage library members", new Color(155, 89, 182));
        membersBtn.addActionListener(e -> navigateToMembers());
        
        CustomButton borrowBtn = createNavigationButton("Borrow/Return", 
            "Process item borrowing and returns", new Color(46, 204, 113));
        borrowBtn.addActionListener(e -> navigateToBorrowReturn());
        
        CustomButton reportsBtn = createNavigationButton("Reports", 
            "View library statistics and reports", new Color(230, 126, 34));
        reportsBtn.addActionListener(e -> navigateToReports());
        
        panel.add(itemsBtn);
        panel.add(membersBtn);
        panel.add(borrowBtn);
        panel.add(reportsBtn);
        
        return panel;
    }
    
    private CustomButton createNavigationButton(String title, String description, Color color) {
        CustomButton button = new CustomButton("<html><div style='text-align:center;'>" +
            "<font size='5' color='white'><b>" + title + "</b></font><br>" +
            "<font size='3' color='#f0f0f0'>" + description + "</font></div></html>");
        
        // Set explicit colors for visibility
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));
        
        // Custom hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void refreshStatistics() {
        try {
            // Total items
            int totalItems = itemService.getAllItems().size();
            totalItemsLabel.setText("<html><div style='text-align:center;'>" +
                    "<font size='6' color='#3498db'><b>" + totalItems + "</b></font><br>" +
                    "<font size='4' color='#666666'>Total Items</font></div></html>");
            
            // Total members
            int totalMembers = memberService.getAllMembers().size();
            totalMembersLabel.setText("<html><div style='text-align:center;'>" +
                    "<font size='6' color='#9b59b6'><b>" + totalMembers + "</b></font><br>" +
                    "<font size='4' color='#666666'>Total Members</font></div></html>");
            
            // Active borrows
            int activeBorrows = borrowService.getActiveBorrows().size();
            activeBorrowsLabel.setText("<html><div style='text-align:center;'>" +
                    "<font size='6' color='#2ecc71'><b>" + activeBorrows + "</b></font><br>" +
                    "<font size='4' color='#666666'>Active Borrows</font></div></html>");
            
            // Overdue items
            int overdueItems = borrowService.getOverdueRecords().size();
            overdueItemsLabel.setText("<html><div style='text-align:center;'>" +
                    "<font size='6' color='#e74c3c'><b>" + overdueItems + "</b></font><br>" +
                    "<font size='4' color='#666666'>Overdue Items</font></div></html>");
            
            // Available items
            long availableItems = itemService.getAllItems().stream()
                .filter(item -> item.isAvailable())
                .count();
            availableItemsLabel.setText("<html><div style='text-align:center;'>" +
                    "<font size='6' color='#f1c40f'><b>" + availableItems + "</b></font><br>" +
                    "<font size='4' color='#666666'>Available Items</font></div></html>");
            
            // Active members
            long activeMembers = memberService.getAllMembers().stream()
                .filter(member -> member.isActive())
                .count();
            activeMembersLabel.setText("<html><div style='text-align:center;'>" +
                    "<font size='6' color='#e67e22'><b>" + activeMembers + "</b></font><br>" +
                    "<font size='4' color='#666666'>Active Members</font></div></html>");
            
            // Add click listeners to stats cards
            addStatCardListeners();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading statistics: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addStatCardListeners() {
        // Total Items -> Items Management
        totalItemsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToItems();
            }
        });
        
        // Total Members -> Members Management
        totalMembersLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToMembers();
            }
        });
        
        // Active Borrows -> Borrow/Return
        activeBorrowsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToBorrowReturn();
            }
        });
        
        // Overdue Items -> Borrow/Return (overdue tab)
        overdueItemsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToBorrowReturnOverdue();
            }
        });
        
        // Available Items -> Items Management
        availableItemsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToItemsAvailable();
            }
        });
        
        // Active Members -> Members Management (active filter)
        activeMembersLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigateToMembersActive();
            }
        });
    }
    
    private void navigateToItems() {
        switchToTab(1);
    }
    
    private void navigateToMembers() {
        switchToTab(2);
    }
    
    private void navigateToBorrowReturn() {
        switchToTab(3);
    }
    
    private void navigateToReports() {
        switchToTab(4);
    }
    
    private void navigateToBorrowReturnOverdue() {
        switchToTab(3);
        JOptionPane.showMessageDialog(this, 
            "Switched to Borrow/Return tab. Overdue items are displayed in the 'Overdue Items' sub-tab.", 
            "Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void navigateToItemsAvailable() {
        switchToTab(1);
        JOptionPane.showMessageDialog(this, 
            "Switched to Items tab. Use the filter to view available items only.", 
            "Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void navigateToMembersActive() {
        switchToTab(2);
        JOptionPane.showMessageDialog(this, 
            "Switched to Members tab. Active members are displayed in the list.", 
            "Navigation", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void switchToTab(int tabIndex) {
        java.awt.Container parent = getParent();
        while (parent != null && !(parent instanceof com.library.ui.MainFrame)) {
            parent = parent.getParent();
        }
        
        if (parent instanceof com.library.ui.MainFrame) {
            com.library.ui.MainFrame mainFrame = (com.library.ui.MainFrame) parent;
            mainFrame.switchToTab(tabIndex);
        }
    }
}
