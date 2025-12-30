package com.library.ui.panels;

import com.library.models.Member;
import com.library.services.MemberService;
import com.library.ui.components.CustomButton;
import com.library.ui.components.CustomTable;
import com.library.ui.dialogs.AddMemberDialog;
import com.library.ui.dialogs.ConfirmDeleteDialog;
import com.library.ui.dialogs.EditMemberDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MemberManagementPanel extends JPanel {
    private CustomTable memberTable;
    private MemberService memberService;
    private JTextField searchField;
    private Color panelBgColor = Color.WHITE;
    private Color primaryColor = new Color(155, 89, 182);
    
    public MemberManagementPanel() {
        memberService = new MemberService();
        initializeUI();
        loadMembers();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(panelBgColor);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel (title + action buttons)
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // ===== Content panel =====
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
        
        CustomButton backButton = new CustomButton("← Back to Dashboard");
        backButton.setButtonType("info");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.addActionListener(e -> navigateToDashboard());
        
        JLabel titleLabel = new JLabel("Member Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel actionPanel = createActionPanel();
        topPanel.add(actionPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(panelBgColor);
        
        CustomButton addButton = new CustomButton("＋ Add New Member");
        CustomButton editButton = new CustomButton("✎ Edit Member");
        CustomButton deleteButton = new CustomButton("🗑 Delete Member");
        CustomButton refreshButton = new CustomButton("↻ Refresh");
        
        // Style buttons
        addButton.setButtonType("success");
        editButton.setButtonType("primary");
        deleteButton.setButtonType("danger");
        
        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DEBUG: Opening Add Member Dialog...");
                showAddMemberDialog();
            }
        });
        
        editButton.addActionListener(e -> showEditMemberDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
        refreshButton.addActionListener(e -> loadMembers());
        
        actionPanel.add(addButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(refreshButton);
        
        return actionPanel;
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(panelBgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                "Search Members",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(100, 100, 100)
            ),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        CustomButton searchButton = new CustomButton("🔍 Search");
        searchButton.addActionListener(e -> searchMembers());
        
        CustomButton clearButton = new CustomButton("Clear");
        clearButton.setButtonType("secondary");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadMembers();
        });
        
        JPanel searchInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchInputPanel.setBackground(panelBgColor);
        searchInputPanel.add(new JLabel("Search by name or ID:"));
        searchInputPanel.add(searchField);
        searchInputPanel.add(searchButton);
        searchInputPanel.add(clearButton);
        
        panel.add(searchInputPanel, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelBgColor);
        
        String[] columns = {"ID", "Name", "Email", "Phone", "Status", "Borrowed Items", "Join Date"};
        memberTable = new CustomTable(columns);
        
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            "Members List",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 100, 100)
        ));
        scrollPane.setBackground(panelBgColor);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JLabel summaryLabel = new JLabel(" ", SwingConstants.RIGHT);
        summaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        summaryLabel.setForeground(new Color(100, 100, 100));
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(summaryLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadMembers() {
        System.out.println("DEBUG: Loading members from service...");
        memberTable.clear();
        List<Member> members = memberService.getAllMembers();
        System.out.println("DEBUG: Service returned " + members.size() + " members");
        
        for (Member member : members) {
            Object[] row = new Object[7];
            row[0] = member.getId();
            row[1] = member.getName();
            row[2] = member.getEmail();
            row[3] = member.getPhone();
            row[4] = member.isActive() ? 
                "<html><font color='#27ae60'><b>● Active</b></font></html>" : 
                "<html><font color='#e74c3c'><b>● Inactive</b></font></html>";
            row[5] = "<html><b>" + member.getBorrowedCount() + "</b> items</html>";
            row[6] = member.getJoinDate() != null ? member.getJoinDate() : "N/A";
            
            memberTable.addRow(row);
        }
        
        System.out.println("DEBUG: Loaded " + members.size() + " members into table");
    }
    
    private void searchMembers() {
        String query = searchField.getText().trim();
        System.out.println("DEBUG: Searching for: '" + query + "'");
        
        if (query.isEmpty()) {
            loadMembers();
            return;
        }
        
        memberTable.clear();
        List<Member> members = memberService.searchByName(query);
        
        // Also search by ID if no results found by name
        if (members.isEmpty()) {
            Member memberById = memberService.findMemberById(query.toUpperCase());
            if (memberById != null) {
                members.add(memberById);
            }
        }
        
        for (Member member : members) {
            Object[] row = new Object[7];
            row[0] = member.getId();
            row[1] = member.getName();
            row[2] = member.getEmail();
            row[3] = member.getPhone();
            row[4] = member.isActive() ? 
                "<html><font color='#27ae60'><b>● Active</b></font></html>" : 
                "<html><font color='#e74c3c'><b>● Inactive</b></font></html>";
            row[5] = "<html><b>" + member.getBorrowedCount() + "</b> items</html>";
            row[6] = member.getJoinDate() != null ? member.getJoinDate() : "N/A";
            
            memberTable.addRow(row);
        }
        
        System.out.println("DEBUG: Found " + members.size() + " members matching '" + query + "'");
    }
    
    private void showAddMemberDialog() {
        System.out.println("DEBUG: Creating AddMemberDialog...");
        
        // Get the parent JFrame
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Create and show the dialog
        AddMemberDialog dialog = new AddMemberDialog(parentFrame);
        dialog.setVisible(true);
        
        // Check if member was added successfully
        if (dialog.isSuccess()) {
            System.out.println("DEBUG: Member added successfully!");
            loadMembers(); // Refresh the table
        } else {
            System.out.println("DEBUG: Member addition cancelled or failed.");
        }
    }
    
    private void showEditMemberDialog() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#f39c12;'>No Selection</h3>"
                + "<p>Please select a member to edit.</p>"
                + "</div></html>", 
                "Select Member", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String memberId = (String) memberTable.getValueAt(selectedRow, 0);
        Member member = memberService.findMemberById(memberId);
        
        if (member == null) {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#e74c3c;'>Member Not Found</h3>"
                + "<p>The selected member no longer exists.</p>"
                + "</div></html>", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get the parent JFrame
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Create and show the dialog
        EditMemberDialog dialog = new EditMemberDialog(parentFrame, member);
        dialog.setVisible(true);
        
        // Check if member was updated successfully
        if (dialog.isSuccess()) {
            System.out.println("DEBUG: Member updated successfully!");
            loadMembers(); // Refresh the table
        }
    }
    
    private void showDeleteDialog() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#f39c12;'>No Selection</h3>"
                + "<p>Please select a member to delete.</p>"
                + "</div></html>", 
                "Select Member", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String memberId = (String) memberTable.getValueAt(selectedRow, 0);
        Member member = memberService.findMemberById(memberId);
        
        if (member == null) {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#e74c3c;'>Member Not Found</h3>"
                + "<p>The selected member no longer exists.</p>"
                + "</div></html>", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if member has borrowed items
        if (member.getBorrowedCount() > 0) {
            JOptionPane.showMessageDialog(this, 
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#e74c3c;'>Cannot Delete Member</h3>"
                + "<p>Member <b>" + member.getName() + "</b> has <b>" 
                + member.getBorrowedCount() + "</b> borrowed items.<br>"
                + "Please return all items before deleting.</p>"
                + "</div></html>", 
                "Cannot Delete", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the parent JFrame
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        // Create and show the confirmation dialog
        ConfirmDeleteDialog dialog = new ConfirmDeleteDialog(parentFrame, member);
        dialog.setVisible(true);
        
        // Check if deletion was confirmed
        if (dialog.isConfirmed()) {
            System.out.println("DEBUG: Deleting member: " + member.getId());
            
            if (memberService.deleteMember(member.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "<html><div style='text-align:center;'>"
                    + "<h3 style='color:#27ae60;'>✓ Member Deleted</h3>"
                    + "<p>Member <b>" + member.getName() + "</b> has been deleted successfully.</p>"
                    + "</div></html>", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadMembers(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(this, 
                    "<html><div style='text-align:center;'>"
                    + "<h3 style='color:#e74c3c;'>Delete Failed</h3>"
                    + "<p>Failed to delete member. Please try again.</p>"
                    + "</div></html>", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("DEBUG: Delete cancelled by user.");
        }
    }
    
    private void navigateToDashboard() {
        // Find the parent MainFrame
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        
        if (parent instanceof JFrame) {
            JFrame frame = (JFrame) parent;
            
            // Find JTabbedPane in the frame
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
