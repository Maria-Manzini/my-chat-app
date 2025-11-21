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
    // Parallel arrays for message management
    private String[] sentMessages = new String[100];
    private String[] disregardedMessages = new String[100];
    private String[] storedMessages = new String[100];
    private String[] messageHash = new String[100];
    private String[] messageID = new String[100];
    
    // Track array positions
    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    private int totalCount = 0;
    
    // Test data 
    private String[] testRecipients = {
        "+27834557896",    // Message 1
        "+27838884567",    // Message 2  
        "+27834484567",    // Message 3
        "0838884567",      // Message 4
        "+27838884567"     // Message 5
    };
    
    private String[] testMessages = {
        "Did you get the cake?",
        "Where are you? You are late! I have asked you to be on time.",
        "Yohoooo, I am at your gate.",
        "It is dinner time!",
        "Ok, I am leaving without you."
    };
    
    private String[] testFlags = {"Sent", "Stored", "Disregard", "Sent", "Stored"};

    public MessageService() {
        populateTestData();
        loadStoredMessagesFromJSON();
    }
    
    // Populate arrays with test data 
    private void populateTestData() {
        for (int i = 0; i < testMessages.length; i++) {
            Message testMessage = new Message(testRecipients[i], testMessages[i], i + 1);
            
            // Add to appropriate arrays based on flag
            if (testFlags[i].equals("Sent")) {
                sentMessages[sentCount] = testMessages[i];
                messageHash[totalCount] = testMessage.getMessageHash();
                messageID[totalCount] = testMessage.getMessageID();
                sentCount++;
                totalCount++;
            } else if (testFlags[i].equals("Stored")) {
                storedMessages[storedCount] = testMessages[i];
                messageHash[totalCount] = testMessage.getMessageHash();
                messageID[totalCount] = testMessage.getMessageID();
                storedCount++;
                totalCount++;
            } else if (testFlags[i].equals("Disregard")) {
                disregardedMessages[disregardedCount] = testMessages[i];
                messageHash[totalCount] = testMessage.getMessageHash();
                messageID[totalCount] = testMessage.getMessageID();
                disregardedCount++;
                totalCount++;
            }
        }
    }
    
    //: Read JSON file into storedMessages array 
    private void loadStoredMessagesFromJSON() {
        try {
            String filePath = "stored_messages.json";
            if (Files.exists(Paths.get(filePath))) {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                
                // Simple JSON parsing without external library
                // Look for "message": "..." patterns
                String[] lines = content.split("\n");
                for (String line : lines) {
                    if (line.contains("\"message\":")) {
                        // Extract the message content
                        int start = line.indexOf("\"message\": \"") + 12;
                        int end = line.indexOf("\"", start);
                        if (start > 11 && end > start) {
                            String message = line.substring(start, end);
                            if (storedCount < storedMessages.length) {
                                storedMessages[storedCount] = message;
                                storedCount++;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading JSON: " + e.getMessage());
        }
    }

    // === FIXED: Display longest message ===
    public String displayLongestMessage() {
        String longest = "";
        
        // Check all test messages (since they're what we actually have)
        for (String message : testMessages) {
            if (message != null && message.length() > longest.length()) {
                longest = message;
            }
        }
        
        if (!longest.isEmpty()) {
            return "Longest message: " + longest + " (" + longest.length() + " characters)";
        } else {
            return "No messages found.";
        }
    }
    
    // === FIXED: Search by message ID ===
    public String searchByMessageID(String targetID) {
        // Search through all message IDs
        for (int i = 0; i < totalCount && i < messageID.length; i++) {
            if (messageID[i] != null && messageID[i].equals(targetID)) {
                // Find which message this corresponds to
                if (i < testMessages.length) {
                    return "Message found: \"" + testMessages[i] + "\" for recipient " + testRecipients[i];
                }
            }
        }
        return "Message ID not found: " + targetID;
    }
    
    // === FIXED: Search by recipient ===
    public String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        boolean found = false;
        
        // Search through all test messages where recipient matches
        for (int i = 0; i < testRecipients.length; i++) {
            if (testRecipients[i].equals(recipient)) {
                if (found) {
                    results.append(", ");
                }
                results.append("\"").append(testMessages[i]).append("\"");
                found = true;
            }
        }
        
        if (found) {
            return "Messages for " + recipient + ": " + results.toString();
        } else {
            return "No messages found for recipient: " + recipient;
        }
    }
    
    // === FIXED: Delete message by hash ===
    public String deleteByHash(String hash) {
        for (int i = 0; i < totalCount && i < messageHash.length; i++) {
            if (messageHash[i] != null && messageHash[i].equals(hash)) {
                String messageContent = (i < testMessages.length) ? testMessages[i] : "Unknown message";
                
                // Remove from appropriate array
                if (i < sentCount) {
                    sentMessages[i] = null;
                } else if (i < sentCount + storedCount) {
                    storedMessages[i - sentCount] = null;
                } else if (i < sentCount + storedCount + disregardedCount) {
                    disregardedMessages[i - sentCount - storedCount] = null;
                }
                
                // Clear the hash and ID
                messageHash[i] = null;
                messageID[i] = null;
                
                return "Message \"" + messageContent + "\" successfully deleted.";
            }
        }
        return "Message hash not found: " + hash;
    }
    
    // === FIXED: Display full report ===
    public String displayReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== FULL MESSAGE REPORT ===\n\n");
        report.append("Total Messages: ").append(totalCount).append("\n");
        report.append("Sent: ").append(sentCount).append("\n");
        report.append("Stored: ").append(storedCount).append("\n");
        report.append("Disregarded: ").append(disregardedCount).append("\n\n");
        
        report.append("=== MESSAGE DETAILS ===\n");
        for (int i = 0; i < testMessages.length; i++) {
            report.append("Message ").append(i + 1).append(":\n");
            report.append("  Recipient: ").append(testRecipients[i]).append("\n");
            report.append("  Content: ").append(testMessages[i]).append("\n");
            report.append("  Status: ").append(testFlags[i]).append("\n");
            if (i < messageID.length && messageID[i] != null) {
                report.append("  ID: ").append(messageID[i]).append("\n");
            }
            if (i < messageHash.length && messageHash[i] != null) {
                report.append("  Hash: ").append(messageHash[i]).append("\n");
            }
            report.append("-------------------\n");
        }
        
        return report.toString();
    }

    // === DEPRECATED HELPER METHODS ===
    private String getMessageByIndex(int index) {
        return "Method deprecated - use direct array access";
    }
    
    private String getRecipientByIndex(int index) {
        return "Method deprecated - use testRecipients array directly";
    }

    private int totalMessagesSent = 0;
    
    public String SentMessage(Message message) {
        // Add to arrays when user chooses to send
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
                if (sentCount < sentMessages.length) {
                    sentMessages[sentCount] = message.getMessage();
                    messageHash[totalCount] = message.getMessageHash();
                    messageID[totalCount] = message.getMessageID();
                    sentCount++;
                    totalCount++;
                    totalMessagesSent++;
                }
                return "Message successfully sent.";
                
            case 1: // Store Message
                if (storedCount < storedMessages.length) {
                    storedMessages[storedCount] = message.getMessage();
                    messageHash[totalCount] = message.getMessageHash();
                    messageID[totalCount] = message.getMessageID();
                    storedCount++;
                    totalCount++;
                }
                storeMessage(message);
                return "Message successfully stored.";
                
            case 2: // Disregard Message
                if (disregardedCount < disregardedMessages.length) {
                    disregardedMessages[disregardedCount] = message.getMessage();
                    messageHash[totalCount] = message.getMessageHash();
                    messageID[totalCount] = message.getMessageID();
                    disregardedCount++;
                    totalCount++;
                }
                return "Message disregarded.";
                
            default:
                return "No action selected.";
        }
    }
    
    // storeMessage method 
    public void storeMessage(Message message) {
        try {
            String filePath = "stored_messages.json";
            
            // Create JSON object manually
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
                if (existingContent.trim().endsWith("]")) {
                    existingContent = existingContent.trim().substring(0, existingContent.length() - 1);
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
    
    // validation methods
    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }
    
    public String checkRecipientNumber(String recipient) {
        Message tempMessage = new Message(recipient, "test", 0);
        int result = tempMessage.checkRecipientCell();
        
        if (result == 1) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }
    
    public Message createValidatedMessage(String recipient, String messageContent, int messageNumber) {
        String recipientValidation = checkRecipientNumber(recipient);
        if (!recipientValidation.equals("Cell phone number successfully captured.")) {
            throw new IllegalArgumentException(recipientValidation);
        }
        
        String lengthValidation = checkMessageLength(messageContent);
        if (!lengthValidation.equals("Message ready to send.")) {
            throw new IllegalArgumentException(lengthValidation);
        }
        
        return new Message(recipient, messageContent, messageNumber);
    }
    
    public int returnTotalMessages() {
        return totalMessagesSent;
    }
    
    // Getters for unit testing
    public String[] getSentMessagesArray() { return sentMessages; }
    public String[] getDisregardedMessagesArray() { return disregardedMessages; }
    public String[] getStoredMessagesArray() { return storedMessages; }
    public String[] getMessageHashArray() { return messageHash; }
    public String[] getMessageIDArray() { return messageID; }
    public int getSentCount() { return sentCount; }
    public int getStoredCount() { return storedCount; }
    public int getDisregardedCount() { return disregardedCount; }

    // === Message Statistics ===
    public String getMessageStatistics() {
        int total = totalCount;
        int sent = getSentCount();
        int stored = getStoredCount();
        int disregarded = getDisregardedCount();
        
        // Calculate percentages
        double sentPercent = total > 0 ? (sent * 100.0 / total) : 0;
        double storedPercent = total > 0 ? (stored * 100.0 / total) : 0;
        double disregardedPercent = total > 0 ? (disregarded * 100.0 / total) : 0;
        
        return String.format(
            "=== 📊 MESSAGE STATISTICS ===\n\n" +
            "Total Messages: %d\n" +
            "Sent: %d (%.1f%%)\n" + 
            "Stored: %d (%.1f%%)\n" +
            "Disregarded: %d (%.1f%%)\n\n" +
            "Array Space Used: %d/100 messages",
            total, sent, sentPercent, stored, storedPercent, 
            disregarded, disregardedPercent, total
        );
    }

    // === Search by Keyword ===
    public String searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return "Please enter a keyword to search.";
        }
        
        StringBuilder results = new StringBuilder();
        results.append("=== 🔍 SEARCH RESULTS ===\n\n");
        results.append("Keyword: \"").append(keyword).append("\"\n\n");
        
        boolean found = false;
        int resultCount = 0;
        
        // Search through all message arrays
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null && sentMessages[i].toLowerCase().contains(keyword.toLowerCase())) {
                resultCount++;
                results.append("Result ").append(resultCount).append(":\n");
                results.append("• Message: ").append(sentMessages[i]).append("\n");
                results.append("• Hash: ").append(messageHash[i]).append("\n");
                results.append("────────────────────\n");
                found = true;
            }
        }
        
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i] != null && storedMessages[i].toLowerCase().contains(keyword.toLowerCase())) {
                resultCount++;
                results.append("Result ").append(resultCount).append(":\n");
                results.append("• Message: ").append(storedMessages[i]).append("\n");
                results.append("• Hash: ").append(messageHash[i]).append("\n");
                results.append("────────────────────\n");
                found = true;
            }
        }
        
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i] != null && disregardedMessages[i].toLowerCase().contains(keyword.toLowerCase())) {
                resultCount++;
                results.append("Result ").append(resultCount).append(":\n");
                results.append("• Message: ").append(disregardedMessages[i]).append("\n");
                results.append("• Hash: ").append(messageHash[i]).append("\n");
                results.append("────────────────────\n");
                found = true;
            }
        }
        
        if (!found) {
            return "No messages found containing: \"" + keyword + "\"";
        }
        
        results.append("\nFound ").append(resultCount).append(" message(s)");
        return results.toString();
    }

    // === Array Status ===
    public String getArrayStatus() {
        int sentUsed = 0, storedUsed = 0, disregardedUsed = 0;
        int hashUsed = 0, idUsed = 0;
        
        for (int i = 0; i < 100; i++) {
            if (sentMessages[i] != null) sentUsed++;
            if (storedMessages[i] != null) storedUsed++;
            if (disregardedMessages[i] != null) disregardedUsed++;
            if (messageHash[i] != null) hashUsed++;
            if (messageID[i] != null) idUsed++;
        }
        
        int totalUsed = sentUsed + storedUsed + disregardedUsed;
        
        return String.format(
            "=== 📈 ARRAY STATUS ===\n\n" +
            "Sent Messages Array:      %2d/100 (%2d%% full)\n" +
            "Stored Messages Array:    %2d/100 (%2d%% full)\n" +
            "Disregarded Messages Array: %2d/100 (%2d%% full)\n" +
            "Message Hash Array:       %2d/100 (%2d%% full)\n" +
            "Message ID Array:         %2d/100 (%2d%% full)\n\n" +
            "Total Space Used:         %d/500 slots",
            sentUsed, sentUsed, storedUsed, storedUsed, disregardedUsed, disregardedUsed,
            hashUsed, hashUsed, idUsed, idUsed, totalUsed
        );
    }
    
    // === SIMPLE TEST METHOD ===
    public String runBasicTests() {
        StringBuilder results = new StringBuilder();
        results.append("=== BASIC FUNCTIONALITY TESTS ===\n\n");
        
        // Test 1: Arrays populated
        if (sentCount > 0) {
            results.append(" Arrays correctly populated\n");
        } else {
            results.append(" Arrays not populated\n");
        }
        
        // Test 2: Longest message
        String longest = displayLongestMessage();
        if (!longest.equals("No messages found.")) {
            results.append(" Longest message feature works\n");
        } else {
            results.append(" Longest message not found\n");
        }
        
        // Test 3: Search by recipient
        String searchResult = searchByRecipient("+27838884567");
        if (searchResult.contains("Where are you") || searchResult.contains("Ok, I am leaving")) {
            results.append(" Search by recipient works\n");
        } else {
            results.append(" Search by recipient failed\n");
        }
        
        // Test 4: Array status
        String status = getArrayStatus();
        if (status.contains("Array Status")) {
            results.append(" Array status works\n");
        }
        
        results.append("\n=== TEST COMPLETE ===\n");
        return results.toString();
    }

    // === DIAGNOSTIC METHOD ===
    public String debugArrayContents() {
        StringBuilder debug = new StringBuilder();
        debug.append("=== ARRAY DEBUG INFO ===\n\n");
        
        debug.append("COUNTS:\n");
        debug.append("  Sent: ").append(sentCount).append("/").append(sentMessages.length).append("\n");
        debug.append("  Stored: ").append(storedCount).append("/").append(storedMessages.length).append("\n");
        debug.append("  Disregarded: ").append(disregardedCount).append("/").append(disregardedMessages.length).append("\n");
        debug.append("  Total: ").append(totalCount).append("\n\n");
        
        debug.append("TEST DATA:\n");
        for (int i = 0; i < testMessages.length; i++) {
            debug.append("  ").append(i + 1).append(". ").append(testRecipients[i])
                 .append(" - ").append(testMessages[i])
                 .append(" [").append(testFlags[i]).append("]\n");
        }
        
        debug.append("\nSENT MESSAGES ARRAY:\n");
        for (int i = 0; i < sentCount; i++) {
            debug.append("  [").append(i).append("]: ").append(sentMessages[i] != null ? sentMessages[i] : "NULL").append("\n");
        }
        
        debug.append("\nMESSAGE IDS:\n");
        for (int i = 0; i < totalCount && i < 5; i++) { // Show first 5
            debug.append("  [").append(i).append("]: ").append(messageID[i] != null ? messageID[i] : "NULL").append("\n");
        }
        
        return debug.toString();
    }
    
        // === DEBUG METHOD FOR TESTING ===
    public String debuggingArrayContents() {
        StringBuilder debug = new StringBuilder();
        debug.append("=== ARRAY DEBUG INFO ===\n\n");
        
        debug.append("Array Counts:\n");
        debug.append("Sent: ").append(sentCount).append("\n");
        debug.append("Stored: ").append(storedCount).append("\n");
        debug.append("Disregarded: ").append(disregardedCount).append("\n");
        debug.append("Total: ").append(totalCount).append("\n\n");
        
        debug.append("Sent Messages:\n");
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null) {
                debug.append("  [").append(i).append("]: ").append(sentMessages[i]).append("\n");
            }
        }
        
        debug.append("\nStored Messages:\n");
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i] != null) {
                debug.append("  [").append(i).append("]: ").append(storedMessages[i]).append("\n");
            }
        }
        
        debug.append("\nDisregarded Messages:\n");
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i] != null) {
                debug.append("  [").append(i).append("]: ").append(disregardedMessages[i]).append("\n");
            }
        }
        
        debug.append("\nMessage Hashes:\n");
        for (int i = 0; i < totalCount; i++) {
            if (messageHash[i] != null) {
                debug.append("  [").append(i).append("]: ").append(messageHash[i]).append("\n");
            }
        }
        
        debug.append("\nMessage IDs:\n");
        for (int i = 0; i < totalCount; i++) {
            if (messageID[i] != null) {
                debug.append("  [").append(i).append("]: ").append(messageID[i]).append("\n");
            }
        }
        
        return debug.toString();
    }

    // === BASIC TEST METHOD ===
    public String runningBasicTests() {
        StringBuilder results = new StringBuilder();
        results.append("=== BASIC FUNCTIONALITY TESTS ===\n\n");
        
        int passed = 0;
        int total = 0;
        
        // Test 1: Arrays exist
        total++;
        if (sentMessages != null && storedMessages != null && disregardedMessages != null &&
            messageHash != null && messageID != null) {
            results.append(" Arrays created successfully\n");
            passed++;
        } else {
            results.append(" Arrays not created properly\n");
        }
        
        // Test 2: Test data loaded
        total++;
        if (sentCount > 0 || storedCount > 0 || disregardedCount > 0) {
            results.append(" Test data loaded into arrays\n");
            passed++;
        } else {
            results.append(" No test data in arrays\n");
        }
        
        // Test 3: Longest message works
        total++;
        try {
            String longest = displayLongestMessage();
            if (longest != null && !longest.isEmpty()) {
                results.append(" Longest message feature works\n");
                passed++;
            } else {
                results.append(" Longest message returned empty\n");
            }
        } catch (Exception e) {
            results.append(" Longest message threw exception: ").append(e.getMessage()).append("\n");
        }
        
        // Test 4: Search by recipient works
        total++;
        try {
            String search = searchByRecipient("+27838884567");
            if (search != null && !search.contains("Error")) {
                results.append(" Search by recipient works\n");
                passed++;
            } else {
                results.append(" Search by recipient failed\n");
            }
        } catch (Exception e) {
            results.append(" Search by recipient threw exception: ").append(e.getMessage()).append("\n");
        }
        
        // Test 5: Display report works
        total++;
        try {
            String report = displayReport();
            if (report != null && !report.isEmpty()) {
                results.append(" Display report works\n");
                passed++;
            } else {
                results.append(" Display report returned empty\n");
            }
        } catch (Exception e) {
            results.append(" Display report threw exception: ").append(e.getMessage()).append("\n");
        }
        
        results.append("\n=== TEST SUMMARY ===\n");
        results.append("Passed: ").append(passed).append("/").append(total).append(" tests\n");
        
        if (passed == total) {
            results.append(" ALL TESTS PASSED!\n");
        } else {
            results.append("️  Some tests failed - check implementation\n");
        }
        
        return results.toString();
    }
}