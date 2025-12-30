package com.library.ui.dialogs;

import javax.swing.*;
import java.awt.*;

public class ReturnDialog extends JDialog {
    // TODO: Implement return dialog
    public ReturnDialog(JFrame parent) {
        super(parent, "Return Item", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Return Dialog - To be implemented", SwingConstants.CENTER), BorderLayout.CENTER);
        
        add(panel);
    }
}
