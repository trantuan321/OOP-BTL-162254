package com.library.ui.panels;

import com.library.models.*;
import com.library.services.*;
import com.library.ui.components.CustomButton;
import com.library.ui.components.CustomTable;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ReportsPanel extends JPanel {
    private ReportService reportService;
    private ItemService itemService;
    private MemberService memberService;
    private BorrowService borrowService;
    
    private JComboBox<String> reportTypeCombo;
    private JSpinner limitSpinner;
    private CustomTable reportTable;
    private JTextArea summaryArea;
    private Color panelBgColor = Color.WHITE;
    
    public ReportsPanel() {
        itemService = new ItemService();
        memberService = new MemberService();
        borrowService = new BorrowService(itemService, memberService);
        reportService = new ReportService(itemService, memberService, borrowService);
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(panelBgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top panel with title and back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelBgColor);
        
        // Back to Dashboard button - FIXED VISIBILITY
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
        JLabel titleLabel = new JLabel("Reports & Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(230, 126, 34));
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(panelBgColor);
        
        // Control panel
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Report display area
        JPanel reportPanel = createReportPanel();
        mainPanel.add(reportPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Report Controls",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(100, 100, 100)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Report type dropdown
        panel.add(new JLabel("Report Type:"));
        reportTypeCombo = new JComboBox<>(new String[]{
            "Top Borrowed Items", 
            "Top Active Members", 
            "Overdue Items Report",
            "Library Statistics",
            "Member Statistics"
        });
        reportTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTypeCombo.setPreferredSize(new Dimension(200, 35));
        panel.add(reportTypeCombo);
        
        // Limit spinner
        panel.add(new JLabel("Limit:"));
        limitSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        limitSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        limitSpinner.setPreferredSize(new Dimension(80, 35));
        panel.add(limitSpinner);
        
        // Generate button - FIXED VISIBILITY
        CustomButton generateButton = new CustomButton("Generate Report");
        generateButton.setBackground(new Color(46, 204, 113));
        generateButton.setForeground(Color.WHITE);
        generateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        generateButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(39, 174, 96), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        generateButton.addActionListener(e -> generateReport());
        panel.add(generateButton);
        
        // Export button - FIXED VISIBILITY
        CustomButton exportButton = new CustomButton("Export to CSV");
        exportButton.setBackground(new Color(52, 152, 219));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        exportButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        exportButton.addActionListener(e -> exportReport());
        panel.add(exportButton);
        
        return panel;
    }
    
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Report Output",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(100, 100, 100)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Table for data display
        String[] columns = {"Rank", "Item/Member", "Borrow Count", "Details"};
        reportTable = new CustomTable(columns);
        JScrollPane tableScroll = new JScrollPane(reportTable);
        tableScroll.setPreferredSize(new Dimension(800, 300));
        
        // Summary area
        summaryArea = new JTextArea(6, 50);
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryArea.setBackground(new Color(250, 250, 250));
        summaryArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        summaryScroll.setPreferredSize(new Dimension(800, 150));
        
        // Split pane for table and summary
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableScroll, summaryScroll);
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(350);
        
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void generateReport() {
        String reportType = (String) reportTypeCombo.getSelectedItem();
        int limit = (Integer) limitSpinner.getValue();
        
        reportTable.clear();
        summaryArea.setText("");
        
        try {
            switch (reportType) {
                case "Top Borrowed Items":
                    generateTopItemsReport(limit);
                    break;
                case "Top Active Members":
                    generateTopMembersReport(limit);
                    break;
                case "Overdue Items Report":
                    generateOverdueReport();
                    break;
                case "Library Statistics":
                    generateLibraryStats();
                    break;
                case "Member Statistics":
                    generateMemberStats();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            summaryArea.setText("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateTopItemsReport(int limit) {
        Map<LibraryItem, Integer> topItems = reportService.getTopBorrowedItems(limit);
        
        reportTable.setColumnIdentifiers(new String[]{"Rank", "Item", "Borrow Count", "Availability"});
        int rank = 1;
        
        for (Map.Entry<LibraryItem, Integer> entry : topItems.entrySet()) {
            LibraryItem item = entry.getKey();
            Object[] row = new Object[4];
            row[0] = rank++;
            row[1] = item.getTitle();
            row[2] = entry.getValue();
            row[3] = item.isAvailable() ? 
                "<html><font color='#27ae60'>Available (" + item.getAvailableQuantity() + ")</font></html>" : 
                "<html><font color='#e74c3c'>Unavailable</font></html>";
            reportTable.addRow(row);
        }
        
        summaryArea.setText("Top " + limit + " Most Borrowed Items\n" +
                           "════════════════════════════════\n" +
                           "Generated on: " + java.time.LocalDate.now() + "\n" +
                           "Total unique items in report: " + topItems.size() + "\n" +
                           "════════════════════════════════\n" +
                           "This report shows the most frequently borrowed items\n" +
                           "in the library system.");
    }
    
    private void generateTopMembersReport(int limit) {
        Map<Member, Integer> topMembers = reportService.getTopActiveMembers(limit);
        
        reportTable.setColumnIdentifiers(new String[]{"Rank", "Member", "Borrow Count", "Status"});
        int rank = 1;
        
        for (Map.Entry<Member, Integer> entry : topMembers.entrySet()) {
            Member member = entry.getKey();
            Object[] row = new Object[4];
            row[0] = rank++;
            row[1] = member.getName();
            row[2] = entry.getValue();
            row[3] = member.isActive() ? 
                "<html><font color='#27ae60'>Active</font></html>" : 
                "<html><font color='#e74c3c'>Inactive</font></html>";
            reportTable.addRow(row);
        }
        
        summaryArea.setText("Top " + limit + " Most Active Members\n" +
                           "════════════════════════════════\n" +
                           "Generated on: " + java.time.LocalDate.now() + "\n" +
                           "Total members in report: " + topMembers.size() + "\n" +
                           "════════════════════════════════\n" +
                           "This report identifies the most active library members\n" +
                           "based on their borrowing history.");
    }
    
    private void generateOverdueReport() {
        List<BorrowRecord> overdue = reportService.getOverdueReport();
        
        reportTable.setColumnIdentifiers(new String[]{"Record ID", "Member", "Item", "Due Date", "Days Overdue"});
        
        for (BorrowRecord record : overdue) {
            Member member = memberService.findMemberById(record.getMemberId());
            LibraryItem item = itemService.findItemById(record.getItemId());
            
            if (member != null && item != null) {
                long daysOverdue = record.getDaysOverdue();
                Object[] row = new Object[5];
                row[0] = record.getRecordId();
                row[1] = member.getName();
                row[2] = item.getTitle();
                row[3] = record.getDueDate();
                if (daysOverdue > 7) {
                    row[4] = "<html><font color='#e74c3c'><b>" + daysOverdue + " days</b></font></html>";
                } else if (daysOverdue > 3) {
                    row[4] = "<html><font color='#f39c12'>" + daysOverdue + " days</font></html>";
                } else {
                    row[4] = "<html><font color='#f1c40f'>" + daysOverdue + " days</font></html>";
                }
                reportTable.addRow(row);
            }
        }
        
        double totalLateFee = overdue.stream()
            .mapToDouble(r -> r.getDaysOverdue() * 1.0)
            .sum();
        
        summaryArea.setText("Overdue Items Report\n" +
                           "════════════════════════════════\n" +
                           "Generated on: " + java.time.LocalDate.now() + "\n" +
                           "Total overdue items: " + overdue.size() + "\n" +
                           "Total estimated late fees: $" + String.format("%.2f", totalLateFee) + "\n" +
                           "════════════════════════════════\n" +
                           "Items overdue more than 7 days are highlighted in red.\n" +
                           "Late fee: $1.00 per day per item.");
    }
    
    private void generateLibraryStats() {
        Map<String, Integer> stats = reportService.getItemTypeStatistics();
        
        reportTable.setColumnIdentifiers(new String[]{"Statistic", "Value"});
        
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Object[] row = new Object[2];
            row[0] = entry.getKey();
            row[1] = entry.getValue();
            reportTable.addRow(row);
        }
        
        List<LibraryItem> allItems = itemService.getAllItems();
        long availableItems = allItems.stream().filter(LibraryItem::isAvailable).count();
        double availabilityRate = allItems.size() > 0 ? (availableItems * 100.0 / allItems.size()) : 0;
        
        summaryArea.setText("Library Statistics Summary\n" +
                           "════════════════════════════════\n" +
                           "Generated on: " + java.time.LocalDate.now() + "\n" +
                           "Total items: " + allItems.size() + "\n" +
                           "Available items: " + availableItems + "\n" +
                           "Availability rate: " + String.format("%.1f%%", availabilityRate) + "\n" +
                           "════════════════════════════════\n" +
                           "This report provides an overview of the library's\n" +
                           "inventory and availability status.");
    }
    
    private void generateMemberStats() {
        Map<String, Integer> stats = reportService.getMemberStatistics();
        
        reportTable.setColumnIdentifiers(new String[]{"Statistic", "Value"});
        
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Object[] row = new Object[2];
            row[0] = entry.getKey();
            row[1] = entry.getValue();
            reportTable.addRow(row);
        }
        
        List<Member> allMembers = memberService.getAllMembers();
        long activeMembers = allMembers.stream().filter(Member::isActive).count();
        List<BorrowRecord> activeBorrows = borrowService.getActiveBorrows();
        double avgBorrows = allMembers.size() > 0 ? 
            activeBorrows.size() / (double) allMembers.size() : 0;
        
        summaryArea.setText("Member Statistics Summary\n" +
                           "════════════════════════════════\n" +
                           "Generated on: " + java.time.LocalDate.now() + "\n" +
                           "Total members: " + allMembers.size() + "\n" +
                           "Active members: " + activeMembers + 
                           " (" + String.format("%.1f%%", (activeMembers * 100.0 / allMembers.size())) + ")\n" +
                           "Active borrows: " + activeBorrows.size() + "\n" +
                           "Average borrows per member: " + String.format("%.1f", avgBorrows) + "\n" +
                           "════════════════════════════════\n" +
                           "This report provides member activity and engagement metrics.");
    }
    
    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        fileChooser.setSelectedFile(new java.io.File("library_report_" + 
            java.time.LocalDate.now() + ".csv"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File file = fileChooser.getSelectedFile();
                java.io.PrintWriter writer = new java.io.PrintWriter(file);
                
                // Write column headers
                for (int i = 0; i < reportTable.getColumnCount(); i++) {
                    writer.print(reportTable.getColumnName(i));
                    if (i < reportTable.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
                
                // Write data rows
                for (int i = 0; i < reportTable.getRowCount(); i++) {
                    for (int j = 0; j < reportTable.getColumnCount(); j++) {
                        Object value = reportTable.getValueAt(i, j);
                        // Remove HTML tags for CSV
                        String cleanValue = value != null ? 
                            value.toString().replaceAll("<[^>]*>", "") : "";
                        writer.print("\"" + cleanValue.replace("\"", "\"\"") + "\"");
                        if (j < reportTable.getColumnCount() - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }
                
                writer.close();
                JOptionPane.showMessageDialog(this, 
                    "Report exported successfully to:\n" + file.getAbsolutePath(), 
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Failed to export report: " + e.getMessage(), 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
