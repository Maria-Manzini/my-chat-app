/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package my.chatapp;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class MessageTest {
    private MessageService messageService;
    
    @BeforeMethod
    public void setUp() {
        messageService = new MessageService();
    }
    
    @AfterMethod
    public void tearDown() {
        // Clean up after each test
        if (messageService != null) {
            messageService.clearMessages();
        }
    }

    @Test
    public void testMessageLengthValidation_Valid() {
        String result = messageService.checkMessageLength("Hello");
        assertEquals(result, "Message ready to send.");
    }
    
    @Test
    public void testMessageLengthValidation_Invalid() {
        String longMessage = "A".repeat(255);
        String result = messageService.checkMessageLength(longMessage);
        assertTrue(result.contains("exceeds 250 characters"));
    }
    
    @Test
    public void testRecipientValidation_Valid() {
        String result = messageService.checkRecipientNumber("+27718693002");
        assertEquals(result, "Cell phone number successfully captured.");
    }
    
    @Test
    public void testRecipientValidation_Invalid() {
        String result = messageService.checkRecipientNumber("invalid");
        assertEquals(result, "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
    }
    
    @Test
    public void testMessageCreation() {
        Message message = new Message("+27718693002", "Test message", 1);
        assertNotNull(message.getMessageID());
        assertEquals(message.getMessageID().length(), 10);
        assertTrue(message.checkMessageID());
    }
    
    @Test
    public void testMessageHashGeneration() {
        Message message = new Message("+27718693002", "Hello World", 1);
        String hash = message.getMessageHash();
        assertNotNull(hash);
        assertTrue(hash.contains(":1-"));
    }
    
    @Test
    public void testTotalMessagesCounter() {
        assertEquals(messageService.returnTotalMessages(), 0);
    }
    
    @Test
    public void testEmptyMessagesList() {
        String result = messageService.printMessages();
        assertEquals(result, "No messages sent yet.");
    }
}