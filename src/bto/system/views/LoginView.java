package bto.system.views;

import bto.system.controllers.UserController;
import bto.system.models.users.User;
import bto.system.exceptions.AuthenticationException;
import java.util.Scanner;

public class LoginView {
    private final UserController userController;
    private final Scanner scanner;

    public LoginView(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public User displayLogin() {
        System.out.println("\n=== Login ===");
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userController.login(nric, password);
            System.out.println("Login successful! Welcome, " + user.getName());
            return user;
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }
}