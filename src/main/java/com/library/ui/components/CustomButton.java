package com.library.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class CustomButton extends JButton {
    
    public CustomButton(String text) {
        super(text);
        setDefaultStyle();
    }
    
    private void setDefaultStyle() {
        // Set default visible colors
        setBackground(new Color(52, 152, 219)); // Blue color
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(41, 128, 185));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(52, 152, 219));
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(new Color(31, 118, 175));
            }
        });
    }
    
    public void setButtonType(String type) {
        switch (type.toLowerCase()) {
            case "success":
                setBackground(new Color(46, 204, 113));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(39, 174, 96), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                updateHoverColors(new Color(39, 174, 96), new Color(46, 204, 113));
                break;
            case "danger":
                setBackground(new Color(231, 76, 60));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(192, 57, 43), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                updateHoverColors(new Color(192, 57, 43), new Color(231, 76, 60));
                break;
            case "warning":
                setBackground(new Color(241, 196, 15));
                setForeground(Color.BLACK);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(243, 156, 18), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                updateHoverColors(new Color(243, 156, 18), new Color(241, 196, 15));
                break;
            case "info":
                setBackground(new Color(52, 152, 219));
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                updateHoverColors(new Color(41, 128, 185), new Color(52, 152, 219));
                break;
            default:
                setDefaultStyle();
        }
    }
    
    private void updateHoverColors(final Color hoverColor, final Color normalColor) {
        // Remove existing listeners
        for (java.awt.event.MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }
        
        // Add new hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(normalColor);
            }
            
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor.darker());
            }
        });
    }
    
    // Helper method to ensure proper border
    public void setCustomBorder(Color borderColor, int padding) {
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(padding, padding * 2, padding, padding * 2)
        ));
    }
}
