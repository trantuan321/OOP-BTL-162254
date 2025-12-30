package com.library.ui;

import com.library.ui.panels.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private Color bgColor = new Color(245, 247, 250); // Light modern background
    private Color panelBgColor = Color.WHITE; // Panel background
    
    public MainFrame() {
        initialize();
    }
    
    private void initialize() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(null);
        getContentPane().setBackground(bgColor);
        
        // Remove menu bar for cleaner look
        setJMenuBar(null);
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create header with title
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane with modern style
        tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setBackground(bgColor);
        tabbedPane.setForeground(new Color(60, 60, 60));
        
        // Add panels
        tabbedPane.addTab("Dashboard", new DashboardPanel());
        tabbedPane.addTab("Items", new ItemManagementPanel());
        tabbedPane.addTab("Members", new MemberManagementPanel());
        tabbedPane.addTab("Borrow/Return", new BorrowReturnPanel());
        tabbedPane.addTab("Reports", new ReportsPanel());
        
        // Style the tabs
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = createStatusPanel();
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Set initial tab to Dashboard
        tabbedPane.setSelectedIndex(0);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Title
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(41, 128, 185));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Modern Library Management Solution");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(bgColor);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // System info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(bgColor);
        
        JLabel dateLabel = new JLabel(new java.text.SimpleDateFormat("EEE, MMM dd yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        
        infoPanel.add(dateLabel);
        
        header.add(infoPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createStatusPanel() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(240, 242, 245));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JLabel statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        
        JLabel versionLabel = new JLabel("v1.0.0 • Modern Edition");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        versionLabel.setForeground(new Color(100, 100, 100));
        
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(versionLabel, BorderLayout.EAST);
        
        return statusPanel;
    }
    
    public void switchToTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(tabIndex);
        }
    }
    
    public void showNotification(String message) {
        // You can implement a notification system here
        System.out.println("Notification: " + message);
    }
}
