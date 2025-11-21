/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package my.chatapp;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class RegistrationLoginTest {
    
    @BeforeClass
    public void setUpClass() {
        Registration.clearRegisteredUsers();
    }
    
    @BeforeMethod
    public void setUp() {
        Registration.clearRegisteredUsers();
    }
    
    @Test
    public void testUsernameValidation() {
        assertTrue(Registration.checkUserName("kyl_1"));
        assertFalse(Registration.checkUserName("invalid")); // No underscore
        assertFalse(Registration.checkUserName("long_username")); // Too long
    }
    
    @Test
    public void testPasswordValidation() {
        assertTrue(Registration.checkPasswordComplexity("Pass@123")); // Has upper, number, special char
        assertFalse(Registration.checkPasswordComplexity("weak")); // Too short
        assertFalse(Registration.checkPasswordComplexity("password")); // No upper, number, special
        assertFalse(Registration.checkPasswordComplexity("PASSWORD1")); // No special char
    }
    
    @Test
    public void testPhoneValidation() {
        assertTrue(Registration.checkCellPhoneNumber("+27821234567")); // Valid SA format
        assertFalse(Registration.checkCellPhoneNumber("082123")); // Too short
        assertFalse(Registration.checkCellPhoneNumber("0821234567")); // Missing +27
        assertFalse(Registration.checkCellPhoneNumber("+27123456789")); // Invalid after +27
    }
    
    
    @Test
    public void testUserRegistration_Failed_InvalidUsername() {
        String result = Registration.registerUser("invalid", "Pass@123", "John", "Doe", "+27821234567");
        assertTrue(result.contains("not correctly formatted")); // Should fail username validation
    }
    
    @Test
    public void testUserRegistration_Failed_InvalidPassword() {
        String result = Registration.registerUser("test_1", "weak", "John", "Doe", "+27821234567");
        assertTrue(result.contains("Password is not correctly formatted")); // Should fail password validation
    }
    
    @Test
    public void testUserRegistration_Failed_InvalidPhone() {
        String result = Registration.registerUser("test_1", "Pass@123", "John", "Doe", "082123");
        assertTrue(result.contains("Cell number is incorrectly formatted")); // Should fail phone validation
    }
    
    @Test
    public void testDuplicateUserRegistration() {
        // Register first user
        Registration.registerUser("test_1", "Pass@123", "John", "Doe", "+27821234567");
        
        // Try to register same username again
        String result = Registration.registerUser("test_1", "Pass@123", "Jane", "Smith", "+27821234568");
        assertTrue(result.contains("Username already exists"));
    }
    
    @Test
    public void testSuccessfulLogin() {
        Registration.registerUser("test_1", "Pass@123", "John", "Doe", "+27821234567");
        assertTrue(Login.loginUser("test_1", "Pass@123"));
    }
    
    @Test
    public void testFailedLogin_WrongPassword() {
        Registration.registerUser("test_1", "Pass@123", "John", "Doe", "+27821234567");
        assertFalse(Login.loginUser("test_1", "wrongpass"));
    }
    
    @Test
    public void testFailedLogin_NonExistentUser() {
        assertFalse(Login.loginUser("nonexistent", "Pass@123"));
    }
    
   @Test
public void testLoginStatusMessages_Success() {
    Registration.registerUser("test_1", "Pass@123", "John", "Doe", "+27821234567");
    String successStatus = Login.returnLoginStatus("test_1", true);
    
    // More flexible assertions
    assertNotNull(successStatus);
    assertTrue(successStatus.startsWith("Welcome"));
    
}
    
    @Test
    public void testLoginStatusMessages_Failed() {
        String failStatus = Login.returnLoginStatus("test_1", false);
        assertEquals(failStatus, "Login failed. Please check your username and password.");
    }
    
    @AfterClass
    public void tearDown() {
        Registration.clearRegisteredUsers();
    }
}