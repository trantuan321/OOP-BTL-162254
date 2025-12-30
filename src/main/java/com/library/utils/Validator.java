package com.library.utils;

import java.util.regex.Pattern;

public class Validator {
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        // Accept various phone formats
        return phone.matches("[0-9\\s\\-\\(\\)\\+]{7,15}");
    }
    
    public static boolean isValidISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) return false;
        // Simple check for 10 or 13 digits
        String cleanIsbn = isbn.replaceAll("[^0-9]", "");
        return cleanIsbn.matches("\\d{10}|\\d{13}");
    }
    
    public static boolean isValidMemberId(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return id.matches("M\\d{3}");
    }
    
    public static boolean isValidItemId(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return id.matches("(B|MG)\\d{3}");
    }
    
    public static boolean isValidYear(int year) {
        return year >= 1900 && year <= java.time.Year.now().getValue();
    }
    
    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }
    
    public static boolean isNonNegativeNumber(int number) {
        return number >= 0;
    }
    
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.trim().length() >= 2 && name.trim().length() <= 50;
    }
    
    public static boolean isValidMemberData(String id, String name, String email, String phone) {
        return isValidMemberId(id) && 
               isValidName(name) && 
               isValidEmail(email) && 
               isValidPhone(phone);
    }
}
