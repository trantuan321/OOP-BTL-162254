package com.library.ui.dialogs;

import com.library.models.Member;
import com.library.ui.components.CustomButton;
import javax.swing.*;
import java.awt.*;

public class ConfirmDeleteDialog extends JDialog {
    private boolean confirmed = false;
    private Member memberToDelete;
    
    public ConfirmDeleteDialog(JFrame parent, Member member) {
        super(parent, "Confirm Delete", true);
        this.memberToDelete = member;
        initialize();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initialize() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(500, 350));
        getContentPane().setBackground(new Color(245, 247, 250));
        
        // Warning Panel
        JPanel warningPanel = createWarningPanel();
        add(warningPanel, BorderLayout.NORTH);
        
        // Details Panel
        JPanel detailsPanel = createDetailsPanel();
        add(detailsPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createWarningPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(231, 76, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel warningIcon = new JLabel("⚠️");
        warningIcon.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        warningIcon.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Confirm Member Deletion");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("This action cannot be undone");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 230, 230));
        
        JPanel textPanel = new JPanel(new BorderLayout(0, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(warningIcon, BorderLayout.WEST);
        panel.add(textPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        
        // Member ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = createDetailLabel("Member ID:");
        panel.add(idLabel, gbc);
        
        gbc.gridx = 1;
        JLabel idValue = createDetailValue(memberToDelete.getId());
        panel.add(idValue, gbc);
        
        // Member Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel nameLabel = createDetailLabel("Full Name:");
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        JLabel nameValue = createDetailValue(memberToDelete.getName());
        panel.add(nameValue, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel emailLabel = createDetailLabel("Email:");
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        JLabel emailValue = createDetailValue(memberToDelete.getEmail());
        panel.add(emailValue, gbc);
        
        // Borrowed Items Warning
        if (memberToDelete.getBorrowedCount() > 0) {
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            JLabel warningLabel = new JLabel(
                "<html><font color='#e74c3c'><b>⚠ WARNING:</b> This member has "
                + memberToDelete.getBorrowedCount() 
                + " borrowed items that must be returned first.</font></html>");
            warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            panel.add(warningLabel, gbc);
            
            // Disable deletion if member has borrowed items
            setDeletionDisabled();
        } else {
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            JLabel infoLabel = new JLabel(
                "<html><font color='#666666'>This member has no borrowed items and can be safely deleted.</font></html>");
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panel.add(infoLabel, gbc);
        }
        
        return panel;
    }
    
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }
    
    private JLabel createDetailValue(String value) {
        JLabel label = new JLabel(value);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }
    
    private void setDeletionDisabled() {
        // This will be called from the MemberManagementPanel
        // to prevent deletion when member has borrowed items
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        CustomButton cancelButton = new CustomButton("← Cancel");
        cancelButton.setButtonType("secondary");
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
        
        CustomButton deleteButton = new CustomButton("🗑 Delete Member");
        deleteButton.setButtonType("danger");
        deleteButton.addActionListener(e -> {
            if (memberToDelete.getBorrowedCount() > 0) {
                JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align:center;'>"
                    + "<h3 style='color:#e74c3c;'>Cannot Delete Member</h3>"
                    + "<p>This member has <b>" + memberToDelete.getBorrowedCount() 
                    + "</b> borrowed items.<br>"
                    + "Please return all items before deleting.</p>"
                    + "</div></html>",
                    "Cannot Delete",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            confirmed = true;
            dispose();
        });
        
        panel.add(cancelButton);
        panel.add(deleteButton);
        
        return panel;
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Member getMemberToDelete() {
        return memberToDelete;
    }
}
