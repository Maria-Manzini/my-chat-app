/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.chatapp;
import java.util.Map;
import javax.swing.JOptionPane;

public class Login {
    public static boolean loginUser(String username, String password) {
        Map<String, User> registeredUsers = Registration.getRegisteredUsers();
        
        if (!registeredUsers.containsKey(username)) {
            return false;
        }
        
        User user = registeredUsers.get(username);
        return user.getPassword().equals(password);
    }
    
    public static String returnLoginStatus(String username, boolean isLoginSuccessful) {
        if (!isLoginSuccessful) {
            return "Login failed. Please check your username and password.";
        }
        
        User user = Registration.getRegisteredUsers().get(username);
        return "Welcome " + user.getFirstName() + ", " + user.getLastName() + " it is great to see you.";
    }
    
    // Method to show login dialogue
    // NOTE: This method is now primarily used for direct login calls
    // The main app uses performLogin() which calls loginUser() and returnLoginStatus()
    public static void showLoginDialogue() {
        JOptionPane.showMessageDialog(null, 
            "=== User Login ===\n" +
            "Please enter your credentials:",
            "Login Information", 
            JOptionPane.INFORMATION_MESSAGE);
            
        String username = JOptionPane.showInputDialog("Enter username:");
        if (username == null) return; // User cancelled
        
        String password = JOptionPane.showInputDialog("Enter password:");
        if (password == null) return;
        
        boolean loginSuccess = loginUser(username, password);
        String status = returnLoginStatus(username, loginSuccess);
        
        JOptionPane.showMessageDialog(null, status, "Login Result", 
            loginSuccess ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        
        // NEW: If login successful, the main app will detect this through the return values
        // The main app now handles the transition to the chat features
    }
}