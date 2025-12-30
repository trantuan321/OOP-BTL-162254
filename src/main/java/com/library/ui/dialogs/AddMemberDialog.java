package com.library.ui.dialogs;

import com.library.models.Member;
import com.library.services.MemberService;
import com.library.ui.components.CustomButton;
import javax.swing.*;
import java.awt.*;

public class AddMemberDialog extends JDialog {
    private MemberService memberService;
    private boolean success = false;
    private Member createdMember = null;
    
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JCheckBox activeCheckbox;
    
    public AddMemberDialog(JFrame parent) {
        super(parent, "Add New Member", true);
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
        panel.setBackground(new Color(155, 89, 182));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel titleLabel = new JLabel("Add New Member");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subtitleLabel = new JLabel("Fill in the member details below");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(230, 230, 230));
        
        JPanel textPanel = new JPanel(new BorderLayout(0, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        panel.add(textPanel, BorderLayout.WEST);
        
        // Icon or decoration
        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setForeground(Color.WHITE);
        panel.add(iconLabel, BorderLayout.EAST);
        
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
        
        // Auto-generate next ID
        int nextId = memberService.getAllMembers().size() + 1;
        String autoId = "M" + String.format("%03d", nextId);
        
        // Member ID Field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel idLabel = createFormLabel("Member ID");
        panel.add(idLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        idField = createFormTextField(autoId);
        panel.add(idField, gbc);
        
        // Name Field
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel nameLabel = createFormLabel("Full Name");
        panel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        nameField = createFormTextField("");
        panel.add(nameField, gbc);
        
        // Email Field
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel emailLabel = createFormLabel("Email Address");
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        emailField = createFormTextField("");
        panel.add(emailField, gbc);
        
        // Phone Field
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel phoneLabel = createFormLabel("Phone Number");
        panel.add(phoneLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        phoneField = createFormTextField("");
        panel.add(phoneField, gbc);
        
        // Status Checkbox
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel statusLabel = createFormLabel("Status");
        panel.add(statusLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        activeCheckbox = new JCheckBox("Active Member");
        activeCheckbox.setSelected(true);
        activeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        activeCheckbox.setBackground(Color.WHITE);
        activeCheckbox.setFocusPainted(false);
        panel.add(activeCheckbox, gbc);
        
        // Info text
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        JLabel infoLabel = new JLabel("<html><font color='#666666' size='2'>"
            + "• Member ID will be auto-generated<br>"
            + "• All fields are required</font></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(infoLabel, gbc);
        
        return panel;
    }
    
    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text + ":");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        return label;
    }
    
    private JTextField createFormTextField(String defaultValue) {
        JTextField field = new JTextField(20);
        field.setText(defaultValue);
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
                    BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
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
        
        CustomButton saveButton = new CustomButton("💾 Save Member");
        saveButton.setButtonType("success");
        saveButton.addActionListener(e -> saveMember());
        
        // Make Save button the default
        getRootPane().setDefaultButton(saveButton);
        
        panel.add(cancelButton);
        panel.add(saveButton);
        
        return panel;
    }
    
    private void saveMember() {
        // Get values
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        boolean active = activeCheckbox.isSelected();
        
        // Validation
        StringBuilder errors = new StringBuilder();
        
        if (id.isEmpty()) {
            errors.append("• Member ID is required\n");
        } else if (!id.matches("M\\d{3}")) {
            errors.append("• Member ID must be in format M001, M002, etc.\n");
        }
        
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
        
        // Check if ID already exists
        Member existing = memberService.findMemberById(id);
        if (existing != null) {
            showErrorDialog("Duplicate ID", 
                "Member ID '" + id + "' already exists.\nPlease use a different ID.");
            return;
        }
        
        // Create and save member
        Member member = new Member(id, name, email, phone);
        member.setActive(active);
        
        if (memberService.addMember(member)) {
            this.createdMember = member;
            this.success = true;
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "<html><div style='text-align:center;'>"
                + "<h3 style='color:#27ae60;'>✓ Member Added Successfully!</h3>"
                + "<p><b>ID:</b> " + id + "<br>"
                + "<b>Name:</b> " + name + "<br>"
                + "<b>Status:</b> " + (active ? "Active" : "Inactive") + "</p>"
                + "</div></html>",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } else {
            showErrorDialog("Save Failed", 
                "Failed to save member. Please try again.");
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
    
    public Member getCreatedMember() {
        return createdMember;
    }
}
