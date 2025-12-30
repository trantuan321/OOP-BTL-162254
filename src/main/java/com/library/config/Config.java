package com.library.config;

public class Config {
    public static final int MAX_BORROW_DAYS = 14;
    public static final int MAX_ITEMS_PER_MEMBER = 5;
    public static final double LATE_FEE_PER_DAY = 1.0;
    
    public static final String DATA_DIR = "data/";
    public static final String ITEMS_FILE = "items.txt";
    public static final String MEMBERS_FILE = "members.txt";
    public static final String BORROW_RECORDS_FILE = "borrow_records.txt";
    
    public static void printWelcome() {
        System.out.println("========================================");
        System.out.println("    LIBRARY MANAGEMENT SYSTEM v1.0     ");
        System.out.println("========================================");
    }
}
