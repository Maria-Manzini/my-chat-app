/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package my.chatapp;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class RegistrationLoginTest {
    
    @BeforeClass
    public void setUp() {
        Registration.clearRegisteredUsers();
    }
    
    @BeforeMethod
    public void beforeTest() {
        Registration.clearRegisteredUsers();
    }
    
    //existing tests
    @Test
    public void testCheckUserName_CorrectlyFormatted() {
        assertTrue(Registration.checkUserName("kyl_1"));
        assertTrue(Registration.checkUserName("ab_c"));
    }
    
    @Test
    public void testCheckUserName_IncorrectlyFormatted() {
        assertFalse(Registration.checkUserName("kyle1"));
        assertFalse(Registration.checkUserName("kyle!!!!!!!"));
        assertFalse(Registration.checkUserName("my_username"));
    }
    
    @Test
    public void testCheckPasswordComplexity_MeetsRequirements() {
        assertTrue(Registration.checkPasswordComplexity("Ch&&sec@ke99!"));
        assertTrue(Registration.checkPasswordComplexity("P@ssw0rd"));
    }
    
    @Test
    public void testCheckPasswordComplexity_DoesNotMeetRequirements() {
        assertFalse(Registration.checkPasswordComplexity("Password1"));
        assertFalse(Registration.checkPasswordComplexity("p@ssword1"));
        assertFalse(Registration.checkPasswordComplexity("P@ssword"));
        assertFalse(Registration.checkPasswordComplexity("P@1"));
        assertFalse(Registration.checkPasswordComplexity("password"));
    }
    
    @Test
    public void testCheckCellPhoneNumber_ValidSouthAfricanNumbers() {
        assertTrue(Registration.checkCellPhoneNumber("+27821234567"));
        assertTrue(Registration.checkCellPhoneNumber("+27761234567"));
        assertTrue(Registration.checkCellPhoneNumber("+27651234567"));
        assertTrue(Registration.checkCellPhoneNumber("+27831234567"));
    }
    
    @Test
    public void testCheckCellPhoneNumber_InvalidSouthAfricanNumbers() {
        assertFalse(Registration.checkCellPhoneNumber("08966533"));
        assertFalse(Registration.checkCellPhoneNumber("0821234567"));
        assertFalse(Registration.checkCellPhoneNumber("+271234567"));
        assertFalse(Registration.checkCellPhoneNumber("+278212345678"));
        assertFalse(Registration.checkCellPhoneNumber("+27801234567"));
        assertFalse(Registration.checkCellPhoneNumber("+447912345678"));
        assertFalse(Registration.checkCellPhoneNumber("+12345678901"));
    }
    
    @Test
    public void testRegisterUser_Successful() {
        String result = Registration.registerUser(
            "kyl_1", "Ch&&sec@ke99!", "Kyle", "Smith", "+27821234567");
        
        assertEquals(result, "User registered successfully!");
        assertTrue(Registration.getRegisteredUsers().containsKey("kyl_1"));
    }
    
    @Test
    public void testRegisterUser_InvalidUsername() {
        String result = Registration.registerUser(
            "kyle!!!!!!!", "Ch&&sec@ke99!", "Kyle", "Smith", "+27821234567");
        
        assertEquals(result, "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
        assertFalse(Registration.getRegisteredUsers().containsKey("kyle!!!!!!!"));
    }
    
    @Test
    public void testRegisterUser_InvalidPassword() {
        String result = Registration.registerUser(
            "kyl_1", "password", "Kyle", "Smith", "+27821234567");
        
        assertEquals(result, "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        assertFalse(Registration.getRegisteredUsers().containsKey("kyl_1"));
    }
    
    @Test
    public void testRegisterUser_InvalidCellNumber() {
        String result = Registration.registerUser(
            "kyl_1", "Ch&&sec@ke99!", "Kyle", "Smith", "0821234567");
        
        assertEquals(result, "Cell number is incorrectly formatted. South African numbers must be in the format: +27XXXXXXXXX (11 digits total, e.g., +27821234567)");
        assertFalse(Registration.getRegisteredUsers().containsKey("kyl_1"));
    }
    
    @Test
    public void testLoginUser_Successful() {
        Registration.registerUser("kyl_1", "Ch&&sec@ke99!", "Kyle", "Smith", "+27821234567");
        assertTrue(Login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }
    
    @Test
    public void testLoginUser_Failed() {
        Registration.registerUser("kyl_1", "Ch&&sec@ke99!", "Kyle", "Smith", "+27821234567");
        assertFalse(Login.loginUser("kyl_1", "wrongpassword"));
        assertFalse(Login.loginUser("nonexistent", "Ch&&sec@ke99!"));
    }
    
    @Test
    public void testReturnLoginStatus_Successful() {
        Registration.registerUser("kyl_1", "Ch&&sec@ke99!", "Kyle", "Smith", "+27821234567");
        String status = Login.returnLoginStatus("kyl_1", true);
        assertEquals(status, "Welcome Kyle, Smith it is great to see you.");
    }
    
    @Test
    public void testReturnLoginStatus_Failed() {
        String status = Login.returnLoginStatus("kyl_1", false);
        assertEquals(status, "Login failed. Please check your username and password.");
    }
    
    @AfterClass
    public void tearDown() {
        Registration.clearRegisteredUsers();
    }
}