/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package my.chatapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author RC_Student_Lab
 */
public class MessageTest {
    private MessageService messageService;
    
    public MessageTest() {
    }
    
    @BeforeEach
    public void setUp() {
        messageService = new MessageService();
    }
    
    @AfterEach
    public void tearDown() {
        messageService.clearMessages();
    }

    // REQUIREMENT: Test message length validation - both success and failure
    @Test
    public void testCheckMessageLength_Valid() {
        System.out.println("checkMessageLength - Valid");
        String shortMessage = "Hello World";
        String result = messageService.checkMessageLength(shortMessage);
        assertEquals("Message ready to send.", result);
    }
    
    @Test
    public void testCheckMessageLength_Invalid() {
        System.out.println("checkMessageLength - Invalid");
        String longMessage = "A".repeat(251);
        String result = messageService.checkMessageLength(longMessage);
        assertTrue(result.contains("Message exceeds 250 characters by"));
        assertTrue(result.contains("please reduce size."));
    }

    // REQUIREMENT: Test recipient number validation
    @Test
    public void testCheckRecipientNumber_Valid() {
        System.out.println("checkRecipientNumber - Valid");
        String validNumber = "+27718693002";
        String result = messageService.checkRecipientNumber(validNumber);
        assertEquals("Cell phone number successfully captured.", result);
    }
    
    @Test
    public void testCheckRecipientNumber_Invalid() {
        System.out.println("checkRecipientNumber - Invalid");
        String invalidNumber = "08575975889";
        String result = messageService.checkRecipientNumber(invalidNumber);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    // REQUIREMENT: Test Message ID generation and validation
    @Test
    public void testCheckMessageID() {
        System.out.println("checkMessageID");
        Message instance = new Message("+27718693002", "Test message", 1);
        boolean result = instance.checkMessageID();
        assertTrue(result); // Should be valid 10-digit ID
    }

    // REQUIREMENT: Test recipient cell validation method
    @Test
    public void testCheckRecipientCell_Valid() {
        System.out.println("checkRecipientCell - Valid");
        Message instance = new Message("+27718693002", "Test message", 1);
        int result = instance.checkRecipientCell();
        assertEquals(1, result); // Should return 1 for valid
    }
    
    @Test
    public void testCheckRecipientCell_Invalid() {
        System.out.println("checkRecipientCell - Invalid");
        Message instance = new Message("082123", "Test message", 1);
        int result = instance.checkRecipientCell();
        assertEquals(-1, result); // Should return -1 for invalid
    }

    // REQUIREMENT: Test Message Hash generation with your test data
    @Test
    public void testCreateMessageHash() {
        System.out.println("createMessageHash");
        Message instance = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight", 1);
        String result = instance.createMessageHash();
        
        // Should be in format: firstTwoID:1-HITONIGHT
        assertTrue(result.matches("^\\d{2}:1-HI.*TONIGHT$"));
        assertTrue(result.length() > 10);
    }
    
    @Test
    public void testCreateMessageHash_TestData2() {
        System.out.println("createMessageHash - Test Data 2");
        Message instance = new Message("08575975889", "Hi Keegan, did you receive the payment?", 2);
        String result = instance.createMessageHash();
        
        // Should be in format: firstTwoID:2-HIPAYMENT?
        assertTrue(result.matches("^\\d{2}:2-HI.*PAYMENT\\?$"));
    }

    // Test message length validation in Message class
    @Test
    public void testIsMessageTooLong_Valid() {
        System.out.println("isMessageTooLong - Valid");
        Message instance = new Message("+27718693002", "Short message", 1);
        boolean result = instance.isMessageTooLong();
        assertFalse(result);
    }
    
    @Test
    public void testIsMessageTooLong_Invalid() {
        System.out.println("isMessageTooLong - Invalid");
        String longMessage = "A".repeat(251);
        Message instance = new Message("+27718693002", longMessage, 1);
        boolean result = instance.isMessageTooLong();
        assertTrue(result);
    }

    @Test
    public void testGetLengthErrorMessage_Valid() {
        System.out.println("getLengthErrorMessage - Valid");
        Message instance = new Message("+27718693002", "Short message", 1);
        String result = instance.getLengthErrorMessage();
        assertEquals("Message ready to send.", result);
    }
    
    @Test
    public void testGetLengthErrorMessage_Invalid() {
        System.out.println("getLengthErrorMessage - Invalid");
        String longMessage = "A".repeat(251);
        Message instance = new Message("+27718693002", longMessage, 1);
        String result = instance.getLengthErrorMessage();
        assertTrue(result.contains("Message exceeds 250 characters by"));
    }

    // Test getters
    @Test
    public void testGetMessageID() {
        System.out.println("getMessageID");
        Message instance = new Message("+27718693002", "Test message", 1);
        String result = instance.getMessageID();
        assertNotNull(result);
        assertEquals(10, result.length()); // Should be 10 digits
        assertTrue(result.matches("^\\d{10}$"));
    }

    @Test
    public void testGetRecipient() {
        System.out.println("getRecipient");
        Message instance = new Message("+27718693002", "Test message", 1);
        String result = instance.getRecipient();
        assertEquals("+27718693002", result);
    }

    @Test
    public void testGetMessage() {
        System.out.println("getMessage");
        Message instance = new Message("+27718693002", "Test message", 1);
        String result = instance.getMessage();
        assertEquals("Test message", result);
    }

    @Test
    public void testGetMessageHash() {
        System.out.println("getMessageHash");
        Message instance = new Message("+27718693002", "Test message", 1);
        String result = instance.getMessageHash();
        assertNotNull(result);
        assertTrue(result.contains(":1-")); // Should contain message number
    }

    @Test
    public void testGetMessageNumber() {
        System.out.println("getMessageNumber");
        Message instance = new Message("+27718693002", "Test message", 5);
        int result = instance.getMessageNumber();
        assertEquals(5, result);
    }

    @Test
    public void testIsSent() {
        System.out.println("isSent");
        Message instance = new Message("+27718693002", "Test message", 1);
        boolean result = instance.isSent();
        assertFalse(result); // Initially not sent
        
        instance.setSent(true);
        assertTrue(instance.isSent()); // After setting to sent
    }

    @Test
    public void testIsStored() {
        System.out.println("isStored");
        Message instance = new Message("+27718693002", "Test message", 1);
        boolean result = instance.isStored();
        assertFalse(result); // Initially not stored
        
        instance.setStored(true);
        assertTrue(instance.isStored()); // After setting to stored
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        Message instance = new Message("+27718693002", "Test message", 1);
        String result = instance.toString();
        assertTrue(result.contains("Message ID:"));
        assertTrue(result.contains("Message Hash:"));
        assertTrue(result.contains("Recipient:"));
        assertTrue(result.contains("Message:"));
    }

    @Test
    public void testGetDisplayFormat() {
        System.out.println("getDisplayFormat");
        Message instance = new Message("+27718693002", "Test message", 1);
        String result = instance.getDisplayFormat();
        assertTrue(result.contains("Message ID:"));
        assertTrue(result.contains("Message Hash:"));
        assertTrue(result.contains("Recipient:"));
        assertTrue(result.contains("Message:"));
    }
    
    // REQUIREMENT: Test MessageService methods
    @Test
    public void testReturnTotalMessages_Initial() {
        System.out.println("returnTotalMessages - Initial");
        int result = messageService.returnTotalMessages();
        assertEquals(0, result);
    }
    
    @Test
    public void testPrintMessages_Empty() {
        System.out.println("printMessages - Empty");
        String result = messageService.printMessages();
        assertEquals("No messages sent yet.", result);
    }
}