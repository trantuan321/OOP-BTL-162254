package com.library.ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class CustomTable extends JTable {
    private DefaultTableModel model;
    
    public CustomTable(String[] columns) {
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };
        
        setModel(model);
        setRowHeight(35);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setGridColor(new Color(230, 230, 230));
        setShowGrid(true);
        setIntercellSpacing(new Dimension(1, 1));
        
        // Style the table
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Style the header
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(51, 51, 51));
        header.setReorderingAllowed(false);
        
        // Set alternating row colors
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                }
                
                setHorizontalAlignment(getColumnAlignment(column));
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                
                return c;
            }
        });
        
        // Selection style
        setSelectionBackground(new Color(220, 240, 255));
        setSelectionForeground(Color.BLACK);
    }
    
    private int getColumnAlignment(int column) {
        // Align numeric columns to the right
        String columnName = getColumnName(column).toLowerCase();
        if (columnName.contains("count") || columnName.contains("quantity") || 
            columnName.contains("year") || columnName.contains("days") ||
            columnName.contains("rank") || columnName.contains("value")) {
            return SwingConstants.RIGHT;
        }
        return SwingConstants.LEFT;
    }
    
    public void addRow(Object[] row) {
        model.addRow(row);
    }
    
    public void clear() {
        model.setRowCount(0);
    }
    
    public void setColumnIdentifiers(String[] columns) {
        model.setColumnIdentifiers(columns);
    }
    
    @Override
    public String getColumnName(int column) {
        return model.getColumnName(column);
    }
}
