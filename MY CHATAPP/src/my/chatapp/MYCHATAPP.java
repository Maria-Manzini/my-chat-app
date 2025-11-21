/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.chatapp;

import javax.swing.JOptionPane;

public class MYCHATAPP {
    private static boolean isLoggedIn = false;
    private static String currentUser = "";
    private static MessageService messageService = new MessageService();
    
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.", "QuickChat", JOptionPane.INFORMATION_MESSAGE);
        
        boolean running = true;
        
        while (running) {
            if (!isLoggedIn) {
                // NUMERIC MENU: Login/Registration
                String menu = 
                    "=== Registration and Login System ===\n" +
                    "South African Phone Number Format: +27XXXXXXXXX (11 digits)\n\n" +
                    "1. Register\n" +
                    "2. Login\n" +
                    "3. Exit\n\n" +
                    "Enter your choice (1-3):";
                
                String choiceStr = JOptionPane.showInputDialog(null, menu, "Main Menu", JOptionPane.QUESTION_MESSAGE);
                if (choiceStr == null) {
                    running = false; // User closed the window
                    continue;
                }
                
                try {
                    int choice = Integer.parseInt(choiceStr);
                    switch (choice) {
                        case 1:
                            Registration.showRegistrationDialogue();
                            break;
                        case 2:
                            performLogin();
                            break;
                        case 3:
                            running = false;
                            JOptionPane.showMessageDialog(null, "Thank you for using the chat app!");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid choice. Please enter 1, 2, or 3.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number (1-3).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // NUMERIC MENU: Main Application Features
                String menu = 
                    "=== QUICKCHAT MAIN MENU ===\n\n" +
                    "Welcome, " + currentUser + "!\n\n" +
                    "Please select an option:\n" +
                    "1.  Send Messages\n" +
                    "2.  Display All Messages\n" + 
                    "3.  Find Longest Message\n" +
                    "4.  Search by Recipient\n" +
                    "5.  Search by Message ID\n" +
                    "6.  Delete by Message Hash\n" +
                    "7.  Display Full Report\n" +
                    "8.  Message Statistics\n" +
                    "9.  Search by Keyword\n" +
                    "10. Array Status\n" +
                    "11. Run Basic Tests\n" +
                    "12. Logout\n\n" +
                    "Enter your choice (1-12):";
                
                String choiceStr = JOptionPane.showInputDialog(null, menu, "Main Menu", JOptionPane.QUESTION_MESSAGE);
                if (choiceStr == null) {
                    running = false; // User closed the window
                    continue;
                }
                
                try {
                    int mainChoice = Integer.parseInt(choiceStr);
                    
                    switch (mainChoice) {
                        case 1: sendMessagesFeature(); break;
                        case 2: displayAllMessages(); break;
                        case 3: findLongestMessage(); break;
                        case 4: searchByRecipient(); break;
                        case 5: searchByMessageID(); break;
                        case 6: deleteByMessageHash(); break;
                        case 7: displayFullReport(); break;
                        case 8: showMessageStatistics(); break;
                        case 9: searchByKeyword(); break;
                        case 10: showArrayStatus(); break;
                        case 11: runBasicTests(); break;
                        case 12: 
                            isLoggedIn = false;
                            currentUser = "";
                            JOptionPane.showMessageDialog(null, "Logged out successfully.");
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Invalid choice. Please enter a number between 1-12.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number (1-12).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    // YOUR ORIGINAL LOGIN METHOD
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
    
    // FEATURE 1: Send Messages 
    private static void sendMessagesFeature() {
        String numMessagesStr = JOptionPane.showInputDialog("How many messages do you wish to send?");
        if (numMessagesStr == null) return;
        
        try {
            int numMessages = Integer.parseInt(numMessagesStr);
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a positive number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
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
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // FEATURE 2: Display All Messages from Arrays
    private static void displayAllMessages() {
        // This will use the arrays from your MessageService
        String[] sentMessages = messageService.getSentMessagesArray();
        String[] messageIDs = messageService.getMessageIDArray();
        String[] messageHashes = messageService.getMessageHashArray();
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALL MESSAGES IN ARRAYS ===\n\n");
        
        boolean foundMessages = false;
        for (int i = 0; i < sentMessages.length; i++) {
            if (sentMessages[i] != null) {
                sb.append("Message ").append(i + 1).append(":\n");
                sb.append("ID: ").append(messageIDs[i] != null ? messageIDs[i] : "N/A").append("\n");
                sb.append("Hash: ").append(messageHashes[i] != null ? messageHashes[i] : "N/A").append("\n");
                sb.append("Message: ").append(sentMessages[i]).append("\n\n");
                foundMessages = true;
            }
        }
        
        if (!foundMessages) {
            sb.append("No messages found in arrays.");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "All Messages in Arrays", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 3: Find Longest Message
    private static void findLongestMessage() {
        String result = messageService.displayLongestMessage();
        JOptionPane.showMessageDialog(null, 
            "=== Longest Sent Message ===\n\n" + result,
            "Longest Message", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 4: Search by Recipient
    private static void searchByRecipient() {
        String recipient = JOptionPane.showInputDialog("Enter recipient phone number to search for:");
        if (recipient == null) return; // User cancelled
        
        String results = messageService.searchByRecipient(recipient);
        JOptionPane.showMessageDialog(null, 
            "=== Messages for " + recipient + " ===\n\n" + results,
            "Search Results", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 5: Search by Message ID
    private static void searchByMessageID() {
        String messageID = JOptionPane.showInputDialog("Enter Message ID to search for:");
        if (messageID == null) return;
        
        String result = messageService.searchByMessageID(messageID);
        JOptionPane.showMessageDialog(null, result, "Message Search", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 6: Delete by Message Hash
    private static void deleteByMessageHash() {
        String messageHash = JOptionPane.showInputDialog("Enter Message Hash to delete:");
        if (messageHash == null) return;
        
        String result = messageService.deleteByHash(messageHash);
        JOptionPane.showMessageDialog(null, result, "Delete Message", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 7: Display Full Report
    private static void displayFullReport() {
        String report = messageService.displayReport();
        JOptionPane.showMessageDialog(null, report, "Full Message Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 8: Message Statistics
    private static void showMessageStatistics() {
        String stats = messageService.getMessageStatistics();
        JOptionPane.showMessageDialog(null, stats, "Message Statistics", JOptionPane.INFORMATION_MESSAGE);
    }

    // FEATURE 9: Search by Keyword
    private static void searchByKeyword() {
        String keyword = JOptionPane.showInputDialog("Enter keyword to search for in messages:");
        if (keyword == null) return; // User cancelled
        
        String results = messageService.searchByKeyword(keyword);
        JOptionPane.showMessageDialog(null, results, "Keyword Search Results", JOptionPane.INFORMATION_MESSAGE);
    }

    // FEATURE 10: Array Status
    private static void showArrayStatus() {
        String status = messageService.getArrayStatus();
        JOptionPane.showMessageDialog(null, status, "Array Status Report", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // FEATURE 11: Test method
    private static void runBasicTests() {
        String results = messageService.runBasicTests();
        JOptionPane.showMessageDialog(null, results, "Basic Tests", JOptionPane.INFORMATION_MESSAGE);
    }
}