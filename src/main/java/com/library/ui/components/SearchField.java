package com.library.ui.components;

import javax.swing.*;
import java.awt.*;

public class SearchField extends JTextField {
    
    public SearchField() {
        this(20);
    }
    
    public SearchField(int columns) {
        super(columns);
        setStyle();
    }
    
    private void setStyle() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        setBackground(Color.WHITE);
    }
    
    public void setPlaceholder(String placeholder) {
        setText(placeholder);
        setForeground(Color.GRAY);
        
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (getText().equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (getText().isEmpty()) {
                    setText(placeholder);
                    setForeground(Color.GRAY);
                }
            }
        });
    }
}
