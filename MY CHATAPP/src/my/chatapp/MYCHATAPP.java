/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package my.chatapp;
import javax.swing.JOptionPane;

public class MYCHATAPP {
    private static boolean isLoggedIn = false;
    private static String currentUser = "";
    // NEW: Add MessageService instance
    private static MessageService messageService = new MessageService();
    
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.", "QuickChat", JOptionPane.INFORMATION_MESSAGE);
        
        boolean running = true;
        
        while (running) {
            if (!isLoggedIn) {
                // Show login/registration menu (your existing code)
                String[] options = {"Register", "Login", "Exit"};
                int choice = JOptionPane.showOptionDialog(null,
                    "=== Registration and Login System ===\n" +
                    "South African Phone Number Format: +27XXXXXXXXX (11 digits)",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
                
                switch (choice) {
                    case 0:
                        Registration.showRegistrationDialogue();
                        break;
                    case 1:
                        performLogin();
                        break;
                    case 2:
                        running = false;
                        JOptionPane.showMessageDialog(null, "Thank you for using the chat app!");
                        break;
                    default:
                        running = false; // User closed the window
                }
            } else {
                // User is logged in - show the main chat menu
                String[] mainOptions = {"Send Messages", "Show Recently Sent Messages", "Quit"};
                int mainChoice = JOptionPane.showOptionDialog(null,
                    "=== QuickChat Main Menu ===\n" +
                    "Welcome back, " + currentUser + "!\n\n" +
                    "Please select an option:\n" +
                    "1) Send Messages\n" + 
                    "2) Show Recently Sent Messages\n" +
                    "3) Quit",
                    "Main Menu",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    mainOptions,
                    mainOptions[0]);
                
                switch (mainChoice) {
                    case 0: // Option 1: Send Messages
                        sendMessagesFeature();
                        break;
                    case 1: // Option 2: Show Recently Sent Messages
    // REQUIREMENT: This feature is still in development - display "Coming Soon"
    JOptionPane.showMessageDialog(null, "Coming Soon....", "Feature in Development", JOptionPane.INFORMATION_MESSAGE);
    break;
                    case 2: // Option 3: Quit
                        isLoggedIn = false;
                        currentUser = "";
                        JOptionPane.showMessageDialog(null, "Logged out successfully.");
                        break;
                    default:
                        running = false; // User closed the window
                }
            }
        }
    }
    
    // Your existing login method (unchanged)
    private static void performLogin() {
        String username = JOptionPane.showInputDialog("Enter username:");
        if (username == null) return;
        
        String password = JOptionPane.showInputDialog("Enter password:");
        if (password == null) return;
        
        boolean loginSuccess = Login.loginUser(username, password);
        String status = Login.returnLoginStatus(username, loginSuccess);
        
        if (loginSuccess) {
            isLoggedIn = true;
            currentUser = username;
            JOptionPane.showMessageDialog(null, status, "Login Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, status, "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // UPDATED: Complete messaging feature with all requirements
    private static void sendMessagesFeature() {
        // REQUIREMENT: User defines how many messages they wish to enter
        String numMessagesStr = JOptionPane.showInputDialog("How many messages do you wish to enter?");
        if (numMessagesStr == null) return;
        
        int numMessages;
        try {
            numMessages = Integer.parseInt(numMessagesStr);
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // REQUIREMENT: For loop for the set number of messages
        for (int i = 0; i < numMessages; i++) {
            JOptionPane.showMessageDialog(null, 
                "Entering Message " + (i + 1) + " of " + numMessages,
                "Message Entry", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Get recipient number with validation
            String recipient;
            while (true) {
                recipient = JOptionPane.showInputDialog(
                    "Enter recipient cell number:\n" +
                    "Format: +27XXXXXXXXX (11 digits total)\n" +
                    "Example: +27718693002");
                if (recipient == null) return; // User cancelled
                
                String validationResult = messageService.checkRecipientNumber(recipient);
                if (validationResult.equals("Cell phone number successfully captured.")) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, validationResult, "Invalid Number", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            // Get message content with length validation
            String messageContent;
            while (true) {
                messageContent = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
                if (messageContent == null) return; // User cancelled
                
                String lengthCheck = messageService.checkMessageLength(messageContent);
                if (lengthCheck.equals("Message ready to send.")) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, lengthCheck, "Message Too Long", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            try {
                // Create validated message
                Message message = messageService.createValidatedMessage(recipient, messageContent, i + 1);
                
                // REQUIREMENT: Display full details using JOptionPane in correct order
                JOptionPane.showMessageDialog(null, 
                    "=== Message Details ===\n\n" +
                    message.getDisplayFormat() + "\n" +
                    "Message Number: " + (i + 1),
                    "Message Created", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // REQUIREMENT: Send/store/disregard choice
                String result = messageService.SentMessage(message);
                JOptionPane.showMessageDialog(null, result, "Message Action Result", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                i--; // Retry this message
            }
        }
        
        // REQUIREMENT: Display total number of messages sent
        int totalSent = messageService.returnTotalMessages();
        JOptionPane.showMessageDialog(null, 
            "=== Message Summary ===\n\n" +
            "Total messages processed: " + numMessages + "\n" +
            "Total messages sent: " + totalSent + "\n" +
            "Thank you for using QuickChat!",
            "Completion Summary", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}