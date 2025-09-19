/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.chatapp;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class Registration {
    private static Map<String, User> registeredUsers = new HashMap<>();
    
    // Regex pattern for South African cell phone validation
    // South African numbers: +27 followed by 9 digits (total 11 digits)
    // or 07 followed by 8 digits (total 10 digits) - but we'll require international format
    private static final Pattern SA_CELL_PHONE_PATTERN = Pattern.compile("^\\+27[1-9]\\d{8}$");
    
    public static boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    
    public static boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return false;
        
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }
        
        return hasUpperCase && hasDigit && hasSpecialChar;
    }
    
    public static boolean checkCellPhoneNumber(String cellNumber) {
        return SA_CELL_PHONE_PATTERN.matcher(cellNumber).matches();
    }
    
    public static String registerUser(String username, String password, String firstName, String lastName, String cellNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell number is incorrectly formatted. South African numbers must be in the format: +27XXXXXXXXX (11 digits total, e.g., +27821234567)";
        }
        
        if (registeredUsers.containsKey(username)) {
            return "Username already exists. Please choose a different username.";
        }
        
        User newUser = new User(username, password, firstName, lastName, cellNumber);
        registeredUsers.put(username, newUser);
        return "User registered successfully!";
    }
    
    public static Map<String, User> getRegisteredUsers() {
        return registeredUsers;
    }
    
    public static void clearRegisteredUsers() {
        registeredUsers.clear();
    }
    
    // Method to show registration dialogue
    public static void showRegistrationDialogue() {
        JOptionPane.showMessageDialog(null, 
            "=== User Registration ===\n" +
            "Please provide the following information:\n" +
            "- First name\n" +
            "- Last name\n" +
            "- Username (must contain '_' and be ≤5 characters)\n" +
            "- Password (≥8 chars, with capital, number, and special char)\n" +
            "- South African cell phone number (format: +27XXXXXXXXX, 11 digits total)",
            "Registration Information", 
            JOptionPane.INFORMATION_MESSAGE);
            
        String firstName = JOptionPane.showInputDialog("Enter first name:");
        if (firstName == null) return; // User cancelled
        
        String lastName = JOptionPane.showInputDialog("Enter last name:");
        if (lastName == null) return;
        
        String username;
        while (true) {
            username = JOptionPane.showInputDialog("Enter username (must contain '_' and be ≤5 characters):");
            if (username == null) return;
            
            if (checkUserName(username)) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.",
                    "Invalid Username", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        String password;
        while (true) {
            password = JOptionPane.showInputDialog("Enter password (≥8 chars, with capital, number, and special char):");
            if (password == null) return;
            
            if (checkPasswordComplexity(password)) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                    "Invalid Password", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        String cellNumber;
        while (true) {
            cellNumber = JOptionPane.showInputDialog("Enter South African cell phone number (format: +27XXXXXXXXX, 11 digits total):");
            if (cellNumber == null) return;
            
            if (checkCellPhoneNumber(cellNumber)) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Cell number is incorrectly formatted. South African numbers must be in the format: +27XXXXXXXXX (11 digits total, e.g., +27821234567)\n\n" +
                    "Valid examples:\n" +
                    "+27821234567\n" +
                    "+27761234567\n" +
                    "+27651234567",
                    "Invalid Cell Number", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        
        String result = registerUser(username, password, firstName, lastName, cellNumber);
        JOptionPane.showMessageDialog(null, result, "Registration Result", JOptionPane.INFORMATION_MESSAGE);
    }
}