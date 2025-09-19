/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package my.chatapp;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class MYCHATAPP {
    public static void main(String[] args) {
        boolean running = true;
        
        while (running) {
            String[] options = {"Register", "Login", "Exit"};
            int choice = JOptionPane.showOptionDialog(null,
                "=== Registration and Login System ===\n" +"South African Phone Number Format: +27XXXXXXXXX (11 digits)",
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
                    Login.showLoginDialogue();
                    break;
                case 2:
                    running = false;
                    JOptionPane.showMessageDialog(null, "Thank you for using the chat app!");
                    break;
                default:
                    running = false; // User closed the window
            }
        }
    }
}