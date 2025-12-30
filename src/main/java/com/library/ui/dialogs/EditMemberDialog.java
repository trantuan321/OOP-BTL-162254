package com.library.ui.dialogs;

import com.library.models.Member;
import com.library.services.MemberService;
import com.library.ui.components.CustomButton;
import javax.swing.*;
import java.awt.*;

public class EditMemberDialog extends JDialog {
    private MemberService memberService;
    private boolean success = false;
    private Member originalMember;
    
    private JLabel idLabel;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JCheckBox activeCheckbox;
    
    public EditMemberDialog(JFrame parent, Member member) {
        super(parent, "Edit Member", true);
        this.memberService = new MemberService();
        this.originalMember = member;
        initialize();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initialize() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(500, 450));
        getContentPane().setBackground(new Color(245, 247, 250));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 152, 219));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel("Edit Member");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Update member information");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(230, 230, 230));
        
        JPanel textPanel = new JPanel(new BorderLayout(0, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(textPanel, BorderLayout.WEST);
        
        // Member ID display
        JLabel idDisplay = new JLabel(originalMember.getId());
        idDisplay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        idDisplay.setForeground(Color.WHITE);
        panel.add(idDisplay, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Member ID (read-only)
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idTextLabel = createFormLabel("Member ID");
        panel.add(idTextLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        idLabel = new JLabel(originalMember.getId());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(new Color(52, 152, 219));
        panel.add(idLabel, gbc);
        
        // Name Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel nameLabel = createFormLabel("Full Name");
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        nameField = createFormTextField(originalMember.getName());
        panel.add(nameField, gbc);
        
        // Email Field
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel emailTextLabel = createFormLabel("Email Address");
        panel.add(emailTextLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        emailField = createFormTextField(originalMember.getEmail());
        panel.add(emailField, gbc);
        
        // Phone Field
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel phoneTextLabel = createFormLabel("Phone Number");
        panel.add(phoneTextLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        phoneField = createFormTextField(originalMember.getPhone());
        panel.add(phoneField, gbc);
        
        // Status Checkbox
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel statusLabel = createFormLabel("Status");
        panel.add(statusLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        activeCheckbox = new JCheckBox("Active Member");
        activeCheckbox.setSelected(originalMember.isActive());
        activeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        activeCheckbox.setBackground(Color.WHITE);
        activeCheckbox.setFocusPainted(false);
        panel.add(activeCheckbox, gbc);
        
        // Statistics info
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JLabel statsLabel = new JLabel("<html><font color='#666666' size='2'>"
            + "• Currently borrowed items: <b>" + originalMember.getBorrowedCount() + "</b><br>"
            + "• Member since: " + originalMember.getJoinDate() + "</font></html>");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(statsLabel, gbc);
        
        return panel;
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text + ":");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }
    
    private JTextField createFormTextField(String value) {
        JTextField field = new JTextField(20);
        field.setText(value);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(new Color(250, 250, 250));
        
        // Add focus listener for better UX
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
                field.setBackground(Color.WHITE);
            }
            
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));
                field.setBackground(new Color(250, 250, 250));
            }
        });
        
        return field;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        CustomButton cancelButton = new CustomButton("Cancel");
        cancelButton.setButtonType("secondary");
        cancelButton.addActionListener(e -> {
            success = false;
            dispose();
        });
        
        CustomButton saveButton = new CustomButton("💾 Save Changes");
        saveButton.setButtonType("success");
        saveButton.addActionListener(e -> saveChanges());
        
        getRootPane().setDefaultButton(saveButton);
        
        panel.add(cancelButton);
        panel.add(saveButton);
        
        return panel;
    }
    
    private void saveChanges() {
        // Get values
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean active = activeCheckbox.isSelected();
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (name.isEmpty()) {
            errors.append("• Member name is required\n");
        } else if (name.length() < 2) {
            errors.append("• Name must be at least 2 characters\n");
        }
        
        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("• Valid email address is required\n");
        }
        
        if (phone.isEmpty() || !phone.matches("\\d{10,11}")) {
            errors.append("• Valid phone number (10-11 digits) is required\n");
        }
        
        // Show errors if any
        if (errors.length() > 0) {
            showErrorDialog("Validation Error", errors.toString());
            return;
        }
        
        // Update member
        originalMember.setName(name);
        originalMember.setEmail(email);
        originalMember.setPhone(phone);
        originalMember.setActive(active);
        
        if (memberService.updateMember(originalMember.getId(), originalMember)) {
            this.success = true;
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#27ae60;'>✓ Member Updated Successfully!</h3>"
                + "<p><b>ID:</b> " + originalMember.getId() + "<br>"
                + "<b>Name:</b> " + name + "</p>"
                + "</div></html>",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } else {
            showErrorDialog("Update Failed", 
                "Failed to update member. Please try again.");
        }
    }
    
    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this,
            "<html><div style='text-align:left;'>"
            + "<h3 style='color:#e74c3c;'>" + title + "</h3>"
            + "<p>" + message.replace("\n", "<br>") + "</p>"
            + "</div></html>",
            title,
            JOptionPane.ERROR_MESSAGE);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Member getUpdatedMember() {
        return originalMember;
    }
}
