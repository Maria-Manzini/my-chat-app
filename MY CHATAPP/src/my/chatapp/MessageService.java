/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.chatapp;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageService {
    // REQUIREMENT: Track all messages and sent messages
    private List<Message> allMessages;
    private List<Message> sentMessages;
    private int totalMessagesSent;
    
    public MessageService() {
        this.allMessages = new ArrayList<>();
        this.sentMessages = new ArrayList<>();
        this.totalMessagesSent = 0;
    }
    
    // REQUIREMENT: String SentMessage() - allow user to choose send, store, or disregard
    public String SentMessage(Message message) {
        // Add to all messages trackings
        allMessages.add(message);
        
        String[] options = {"Send Message", "Store Message", "Disregard Message"};
        int choice = JOptionPane.showOptionDialog(null,
            "What would you like to do with this message?\n\n" + message.getDisplayFormat(),
            "Message Action",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
        
        switch (choice) {
            case 0: // Send Message
                message.setSent(true);
                sentMessages.add(message);
                totalMessagesSent++;
                return "Message successfully sent.";
                
            case 1: // Store Message
                message.setStored(true);
                storeMessage(message);
                return "Message successfully stored.";
                
            case 2: // Disregard Message
                allMessages.remove(message); // Remove from tracking
                return "Press 0 to delete message.";
                
            default:
                return "No action selected.";
        }
    }
    
    // REQUIREMENT: storeMessage() method to store messages in JSON
    public void storeMessage(Message message) {
        try {
            String filePath = "stored_messages.json";
            
            // Create JSON object manually (simple implementation)
            String jsonMessage = String.format(
                "{\n" +
                "  \"messageID\": \"%s\",\n" +
                "  \"recipient\": \"%s\",\n" +
                "  \"message\": \"%s\",\n" +
                "  \"messageHash\": \"%s\",\n" +
                "  \"messageNumber\": %d,\n" +
                "  \"sent\": %b,\n" +
                "  \"stored\": %b,\n" +
                "  \"timestamp\": \"%s\"\n" +
                "}",
                message.getMessageID(),
                message.getRecipient(),
                escapeJsonString(message.getMessage()),
                message.getMessageHash(),
                message.getMessageNumber(),
                message.isSent(),
                message.isStored(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            
            // Read existing content if file exists
            String existingContent = "";
            if (Files.exists(Paths.get(filePath))) {
                existingContent = new String(Files.readAllBytes(Paths.get(filePath)));
                // Remove closing bracket if it exists
                if (existingContent.trim().endsWith("]")) {
                    existingContent = existingContent.trim().substring(0, existingContent.length() - 1);
                    // Add comma if there are existing entries
                    if (!existingContent.trim().endsWith("[")) {
                        existingContent += ",";
                    }
                }
            } else {
                existingContent = "[";
            }
            
            // Write back to file
            String newContent = existingContent + "\n" + jsonMessage + "\n]";
            Files.write(Paths.get(filePath), newContent.getBytes());
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, 
                "Error storing message: " + e.getMessage(), 
                "Storage Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to escape JSON strings
    private String escapeJsonString(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    // REQUIREMENT: String printMessages() - returns list of all messages sent
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== Recently Sent Messages ===\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            sb.append("Message ").append(i + 1).append(":\n");
            sb.append(msg.getDisplayFormat()).append("\n\n");
        }
        return sb.toString();
    }
    
    // REQUIREMENT: Int returnTotalMessages() - returns total number of messages sent
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // REQUIREMENT: Validation methods for unit tests
    
    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }
    
    public String checkRecipientNumber(String recipient) {
        // Create a temporary message to use its validation method
        Message tempMessage = new Message(recipient, "test", 0);
        int result = tempMessage.checkRecipientCell();
        
        if (result == 1) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    // Method to validate and create a message (for use in main app)
    public Message createValidatedMessage(String recipient, String messageContent, int messageNumber) {
        // Validate recipient
        String recipientValidation = checkRecipientNumber(recipient);
        if (!recipientValidation.equals("Cell phone number successfully captured.")) {
            throw new IllegalArgumentException(recipientValidation);
        }
        
        // Validate message length
        String lengthValidation = checkMessageLength(messageContent);
        if (!lengthValidation.equals("Message ready to send.")) {
            throw new IllegalArgumentException(lengthValidation);
        }
        
        // Create and return the message
        return new Message(recipient, messageContent, messageNumber);
    }
    
    // Getters for testing and debugging
    public List<Message> getAllMessages() {
        return allMessages;
    }
    
    public List<Message> getSentMessages() {
        return sentMessages;
    }
    
    // Method to clear all messages (for testing)
    public void clearMessages() {
        allMessages.clear();
        sentMessages.clear();
        totalMessagesSent = 0;
    }
}
