package com.library.ui.dialogs;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BorrowDialog extends JDialog {
    // TODO: Implement borrow dialog
    
    public BorrowDialog(JFrame parent) {
        super(parent, "Borrow Item", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Borrow Dialog - To be implemented", SwingConstants.CENTER), BorderLayout.CENTER);
        
        add(panel);
    }
}
