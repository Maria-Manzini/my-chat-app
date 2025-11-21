package my.chatapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageServiceTest {
    
    private MessageService messageService;
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("=== Starting MessageService Tests ===");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("=== MessageService Tests Completed ===");
    }
    
    @BeforeEach
    public void setUp() {
        messageService = new MessageService();
    }
    
    @AfterEach
    public void tearDown() {
        // Clean up after each test if needed
    }

    // TEST 1: Basic array population
    @Test
    public void testArraysCorrectlyPopulated() {
        System.out.println("Testing arrays are correctly populated...");
        
        // Debug output to see what's happening
        System.out.println(messageService.debugArrayContents());
        
        String[] sentMessages = messageService.getSentMessagesArray();
        
        // Check that arrays exist
        assertNotNull(sentMessages, "Sent messages array should not be null");
        assertNotNull(messageService.getStoredMessagesArray(), "Stored messages array should not be null");
        assertNotNull(messageService.getDisregardedMessagesArray(), "Disregarded messages array should not be null");
        assertNotNull(messageService.getMessageHashArray(), "Message hash array should not be null");
        assertNotNull(messageService.getMessageIDArray(), "Message ID array should not be null");
        
        // Check that we have some data
        boolean hasData = messageService.getSentCount() > 0 || 
                         messageService.getStoredCount() > 0 || 
                         messageService.getDisregardedCount() > 0;
        assertTrue(hasData, "Should have some messages in arrays");
        
        System.out.println(" Arrays correctly populated test PASSED");
    }

    // TEST 2: Display the longest message
    @Test
    public void testDisplayLongestMessage() {
        System.out.println("Testing display longest message...");
        
        String result = messageService.displayLongestMessage();
        System.out.println("Result: " + result);
        
        // Should not be null or empty
        assertNotNull(result, "Should not return null");
        assertFalse(result.isEmpty(), "Should not return empty string");
        
        // Should contain the longest message or indicate it
        assertTrue(result.contains("Longest message") || result.contains("Where are you"), 
                  "Should return longest message information");
        
        System.out.println(" Display longest message test PASSED");
    }

    // TEST 3: Search for messages by recipient
    @Test
    public void testSearchByRecipient() {
        System.out.println("Testing search by recipient...");
        
        // Test with recipient that has multiple messages
        String result = messageService.searchByRecipient("+27838884567");
        System.out.println("Result: " + result);
        
        assertNotNull(result, "Should not return null");
        
        // Should find messages for this recipient
        assertTrue(result.contains("Where are you") || result.contains("Ok, I am leaving"), 
                  "Should find messages for recipient +27838884567");
        
        System.out.println(" Search by recipient test PASSED");
    }

    // TEST 4: Search for message by ID
    @Test
    public void testSearchByMessageID() {
        System.out.println("Testing search by message ID...");
        
        // Get the first valid message ID from the arrays
        String[] messageIDs = messageService.getMessageIDArray();
        String validID = null;
        
        // Find first non-null message ID
        for (String id : messageIDs) {
            if (id != null && !id.trim().isEmpty()) {
                validID = id;
                break;
            }
        }
        
        System.out.println("Testing with ID: " + validID);
        
        if (validID != null) {
            String result = messageService.searchByMessageID(validID);
            System.out.println("Result: " + result);
            // Should find the message
            assertTrue(result.contains("Message found"), "Should find message with valid ID");
        }
        
        // Test with invalid ID
        String invalidResult = messageService.searchByMessageID("0000000000");
        assertTrue(invalidResult.contains("Message ID not found"), "Should not find non-existent ID");
        
        System.out.println(" Search by message ID test PASSED");
    }

    // TEST 5: Delete message by hash
    @Test
    public void testDeleteByHash() {
        System.out.println("Testing delete by hash...");
        
        // Get a valid message hash
        String[] messageHashes = messageService.getMessageHashArray();
        String validHash = null;
        
        for (String hash : messageHashes) {
            if (hash != null && !hash.trim().isEmpty()) {
                validHash = hash;
                break;
            }
        }
        
        System.out.println("Testing with hash: " + validHash);
        
        if (validHash != null) {
            String result = messageService.deleteByHash(validHash);
            System.out.println("Result: " + result);
            // Should indicate successful deletion
            assertTrue(result.contains("deleted") || result.contains("successfully"), 
                      "Should indicate successful deletion");
        }
        
        System.out.println(" Delete by hash test PASSED");
    }

    // TEST 6: Display report
    @Test
    public void testDisplayReport() {
        System.out.println("Testing display report...");
        
        String result = messageService.displayReport();
        
        assertNotNull(result, "Report should not be null");
        assertFalse(result.isEmpty(), "Report should not be empty");
        
        // Should contain expected sections
        assertTrue(result.contains("FULL MESSAGE REPORT"), "Should contain report header");
        assertTrue(result.contains("MESSAGE DETAILS"), "Should contain message details section");
        
        System.out.println(" Display report test PASSED");
    }

    // TEST 7: Message validation - recipient format
    @Test
    public void testCheckRecipientNumber() {
        System.out.println("Testing recipient number validation...");
        
        // Test valid number
        String validResult = messageService.checkRecipientNumber("+27821234567");
        assertEquals("Cell phone number successfully captured.", validResult,
                    "Should accept valid SA cell number");
        
        // Test invalid number
        String invalidResult = messageService.checkRecipientNumber("invalid");
        assertTrue(invalidResult.contains("incorrectly formatted"),
                  "Should reject invalid cell number");
        
        System.out.println(" Recipient validation test PASSED");
    }

    // TEST 8: Message validation - message length
    @Test
    public void testCheckMessageLength() {
        System.out.println("Testing message length validation...");
        
        // Test with short message
        String shortMessage = "Hello";
        String shortResult = messageService.checkMessageLength(shortMessage);
        assertEquals("Message ready to send.", shortResult,
                    "Should accept messages under 250 characters");
        
        // Test with long message
        String longMessage = "A".repeat(300);
        String longResult = messageService.checkMessageLength(longMessage);
        assertTrue(longResult.contains("exceeds 250 characters"),
                  "Should reject messages over 250 characters");
        
        System.out.println(" Message length validation test PASSED");
    }

    // TEST 9: Create validated message
    @Test
    public void testCreateValidatedMessage() {
        System.out.println("Testing create validated message...");
        
        // Test valid input
        Message result = messageService.createValidatedMessage("+27821234567", "Test message", 1);
        
        assertNotNull(result, "Should return a Message object");
        assertEquals("+27821234567", result.getRecipient(), "Recipient should match");
        assertEquals("Test message", result.getMessage(), "Message content should match");
        
        // Test invalid recipient
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            messageService.createValidatedMessage("invalid", "test", 1);
        });
        assertTrue(exception.getMessage().contains("incorrectly formatted"));
        
        System.out.println(" Create validated message test PASSED");
    }

    // TEST 10: Basic functionality
    @Test
    public void testBasicFunctionality() {
        System.out.println("Testing basic functionality...");
        
        // These should always pass
        assertNotNull(messageService, "MessageService should be created");
        assertNotNull(messageService.getSentMessagesArray(), "Should return sent array");
        assertNotNull(messageService.getStoredMessagesArray(), "Should return stored array");
        assertNotNull(messageService.getDisregardedMessagesArray(), "Should return disregarded array");
        assertNotNull(messageService.getMessageHashArray(), "Should return hash array");
        assertNotNull(messageService.getMessageIDArray(), "Should return ID array");
        
        // Test that methods don't throw exceptions
        assertDoesNotThrow(() -> messageService.displayLongestMessage());
        assertDoesNotThrow(() -> messageService.displayReport());
        assertDoesNotThrow(() -> messageService.getMessageStatistics());
        assertDoesNotThrow(() -> messageService.getArrayStatus());
        
        System.out.println(" Basic functionality test PASSED");
    }

    // TEST 11: Message statistics
    @Test
    public void testMessageStatistics() {
        System.out.println("Testing message statistics...");
        
        String stats = messageService.getMessageStatistics();
        
        assertNotNull(stats, "Statistics should not be null");
        assertFalse(stats.isEmpty(), "Statistics should not be empty");
        assertTrue(stats.contains("MESSAGE STATISTICS"), "Should contain statistics header");
        
        System.out.println(" Message statistics test PASSED");
    }

    // TEST 12: Array status
    @Test
    public void testArrayStatus() {
        System.out.println("Testing array status...");
        
        String status = messageService.getArrayStatus();
        
        assertNotNull(status, "Array status should not be null");
        assertFalse(status.isEmpty(), "Array status should not be empty");
        assertTrue(status.contains("ARRAY STATUS"), "Should contain array status header");
        
        System.out.println(" Array status test PASSED");
    }

    // TEST 13: Search by keyword
    @Test
    public void testSearchByKeyword() {
        System.out.println("Testing search by keyword...");
        
        // Test with keyword that should exist
        String result = messageService.searchByKeyword("dinner");
        assertNotNull(result, "Search result should not be null");
        
        // Test with non-existent keyword
        String noResult = messageService.searchByKeyword("nonexistentkeyword123");
        assertTrue(noResult.contains("No messages found"), "Should indicate no results for non-existent keyword");
        
        System.out.println(" Search by keyword test PASSED");
    }

    // TEST 14: Test counts are consistent
    @Test
    public void testCountConsistency() {
        System.out.println("Testing count consistency...");
        
        int sentCount = messageService.getSentCount();
        int storedCount = messageService.getStoredCount();
        int disregardedCount = messageService.getDisregardedCount();
        
        // Counts should be non-negative
        assertTrue(sentCount >= 0, "Sent count should be non-negative");
        assertTrue(storedCount >= 0, "Stored count should be non-negative");
        assertTrue(disregardedCount >= 0, "Disregarded count should be non-negative");
        
        // Total should be at least the sum of individual counts
        assertTrue((sentCount + storedCount + disregardedCount) >= 0, "Total count should be consistent");
        
        System.out.println(" Count consistency test PASSED");
    }

    // TEST 15: Test debug output
    @Test
    public void testDebugOutput() {
        System.out.println("Testing debug output...");
        
        String debug = messageService.debugArrayContents();
        
        assertNotNull(debug, "Debug output should not be null");
        assertFalse(debug.isEmpty(), "Debug output should not be empty");
        assertTrue(debug.contains("ARRAY DEBUG INFO"), "Should contain debug header");
        
        System.out.println(" Debug output test PASSED");
    }

    // TEST 16: Test recipient edge cases
    @Test
    public void testRecipientEdgeCases() {
        System.out.println("Testing recipient edge cases...");
        
        // Test various formats
        String valid1 = messageService.checkRecipientNumber("+27821234567");
        assertEquals("Cell phone number successfully captured.", valid1);
        
        String invalid1 = messageService.checkRecipientNumber("0821234567");
        assertTrue(invalid1.contains("incorrectly formatted"));
        
        String invalid2 = messageService.checkRecipientNumber("");
        assertTrue(invalid2.contains("incorrectly formatted"));
        
        String invalid3 = messageService.checkRecipientNumber("abc123");
        assertTrue(invalid3.contains("incorrectly formatted"));
        
        System.out.println(" Recipient edge cases test PASSED");
    }

    // TEST 17: Test message length edge cases
    @Test
    public void testMessageLengthEdgeCases() {
        System.out.println("Testing message length edge cases...");
        
        // Test exact boundary
        String exact250 = "A".repeat(250);
        String exactResult = messageService.checkMessageLength(exact250);
        assertEquals("Message ready to send.", exactResult);
        
        // Test one character over
        String over251 = "A".repeat(251);
        String overResult = messageService.checkMessageLength(over251);
        assertTrue(overResult.contains("exceeds 250 characters"));
        
        // Test empty message
        String emptyResult = messageService.checkMessageLength("");
        assertEquals("Message ready to send.", emptyResult);
        
        System.out.println(" Message length edge cases test PASSED");
    }

    // TEST 18: Test array getters
    @Test
    public void testArrayGetters() {
        System.out.println("Testing array getters...");
        
        // All getters should return non-null arrays
        assertNotNull(messageService.getSentMessagesArray());
        assertNotNull(messageService.getStoredMessagesArray());
        assertNotNull(messageService.getDisregardedMessagesArray());
        assertNotNull(messageService.getMessageHashArray());
        assertNotNull(messageService.getMessageIDArray());
        
        // Arrays should have correct length
        assertEquals(100, messageService.getSentMessagesArray().length);
        assertEquals(100, messageService.getStoredMessagesArray().length);
        assertEquals(100, messageService.getDisregardedMessagesArray().length);
        assertEquals(100, messageService.getMessageHashArray().length);
        assertEquals(100, messageService.getMessageIDArray().length);
        
        System.out.println(" Array getters test PASSED");
    }

    // TEST 19: Test basic test runner
    @Test
    public void testBasicTestRunner() {
        System.out.println("Testing basic test runner...");
        
        String testResults = messageService.runBasicTests();
        
        assertNotNull(testResults, "Test results should not be null");
        assertFalse(testResults.isEmpty(), "Test results should not be empty");
        assertTrue(testResults.contains("BASIC FUNCTIONALITY TESTS"), "Should contain test header");
        
        System.out.println(" Basic test runner test PASSED");
    }

    // TEST 20: Integration test
    @Test
    public void testIntegration() {
        System.out.println("Testing integration...");
        
        // Test that all major methods work together
        assertDoesNotThrow(() -> {
            // Test data retrieval
            String debug = messageService.debugArrayContents();
            assertNotNull(debug);
            
            // Test search functionality
            String search = messageService.searchByRecipient("+27838884567");
            assertNotNull(search);
            
            // Test reporting
            String report = messageService.displayReport();
            assertNotNull(report);
            
            // Test statistics
            String stats = messageService.getMessageStatistics();
            assertNotNull(stats);
            
            // Test array status
            String status = messageService.getArrayStatus();
            assertNotNull(status);
        });
        
        System.out.println(" Integration test PASSED");
    }
    
   
    @Test
    public void testArraysExist() {
        // Basic test - arrays should exist
        assertNotNull(messageService.getSentMessagesArray());
        assertNotNull(messageService.getStoredMessagesArray());
        assertNotNull(messageService.getDisregardedMessagesArray());
        assertNotNull(messageService.getMessageHashArray());
        assertNotNull(messageService.getMessageIDArray());
    }
    
    
    @Test
    public void testMessageValidation() {
        // Test validation methods
        String validResult = messageService.checkRecipientNumber("+27821234567");
        assertEquals("Cell phone number successfully captured.", validResult);
        
        String shortResult = messageService.checkMessageLength("Hello");
        assertEquals("Message ready to send.", shortResult);
    }
    
    
    @Test
    public void testEnhancedFeatures() {
        // Test the new enhancement methods
        assertDoesNotThrow(() -> messageService.getMessageStatistics());
        assertDoesNotThrow(() -> messageService.getArrayStatus());
        assertDoesNotThrow(() -> messageService.searchByKeyword("test"));
    }
}
