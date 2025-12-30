package com.library;

import com.library.ui.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        logger.info("Starting Library Management System v1.0.0");
        
        // Set modern look and feel
        try {
            logger.info("Setting up FlatLaf theme");
            FlatLightLaf.setup();
            
            // Additional UI settings
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 5);
            
            logger.info("UI theme setup completed");
        } catch (Exception ex) {
            logger.error("Failed to setup FlatLaf, using system theme", ex);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.error("Failed to set system look and feel", e);
            }
        }
        
        // Run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                logger.info("Creating main application window");
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                logger.info("Application started successfully");
                
                // Log system information
                logSystemInfo();
                
            } catch (Exception e) {
                logger.error("Failed to start application", e);
                JOptionPane.showMessageDialog(null,
                    "Failed to start application:\n" + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
    
    private static void logSystemInfo() {
        logger.info("=== System Information ===");
        logger.info("Java Version: {}", System.getProperty("java.version"));
        logger.info("Java Home: {}", System.getProperty("java.home"));
        logger.info("OS: {} {}", System.getProperty("os.name"), System.getProperty("os.version"));
        logger.info("User: {}", System.getProperty("user.name"));
        logger.info("Working Directory: {}", System.getProperty("user.dir"));
        logger.info("Available Processors: {}", Runtime.getRuntime().availableProcessors());
        logger.info("Max Memory: {} MB", Runtime.getRuntime().maxMemory() / (1024 * 1024));
        logger.info("==========================");
    }
}
