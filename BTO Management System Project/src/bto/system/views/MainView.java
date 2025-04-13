//package bto.system.views;
//
//import bto.system.controllers.UserController;
//import bto.system.controllers.ProjectController;
//import bto.system.models.users.*;
//import java.util.Scanner;
//
//public class MainView {
//    private final UserController userController;
//    private final ProjectController projectController;
//    private final Scanner scanner;
//    private final LoginView loginView;
//    private ApplicantView applicantView;
//    private HDBOfficerView officerView;
//    private HDBManagerView managerView;
//
//    public MainView(UserController userController, ProjectController projectController) {
//        this.userController = userController;
//        this.projectController = projectController;
//        this.scanner = new Scanner(System.in);
//        this.loginView = new LoginView(userController, scanner);
//    }
//
//    public void display() {
//        boolean exit = false;
//
//        while (!exit) {
//            System.out.println("\n=== BTO Management System ===");
//            System.out.println("1. Login");
//            System.out.println("2. Exit");
//            System.out.print("Enter your choice: ");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//
//            switch (choice) {
//                case 1:
//                    handleLogin();
//                    break;
//                case 2:
//                    exit = true;
//                    System.out.println("Exiting system...");
//                    break;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }
//
//    private void handleLogin() {
//        try {
//            User user = loginView.displayLogin();
//
//            if (user != null) {
//                switch (user.getUserType()) {
//                    case "Applicant":
//                        applicantView = new ApplicantView(userController, projectController, scanner);
//                        applicantView.displayMenu((Applicant) user);
//                        break;
//                    case "HDBOfficer":
//                        officerView = new HDBOfficerView(userController, projectController, scanner);
//                        officerView.displayMenu((HDBOfficer) user);
//                        break;
//                    case "HDBManager":
//                        managerView = new HDBManagerView(userController, projectController, scanner);
//                        managerView.displayMenu((HDBManager) user);
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//}

package bto.system.views;

import bto.system.controllers.*;
import bto.system.models.users.*;
import java.util.Scanner;

public class MainView {
    private final UserController userController;
    private final ProjectController projectController;
    private final ApplicationController applicationController;
    private final EnquiryController enquiryController;
    private final OfficerController officerController;
    private final Scanner scanner;
    private final LoginView loginView;

    public MainView(UserController userController,
                    ProjectController projectController,
                    ApplicationController applicationController,
                    EnquiryController enquiryController,
                    OfficerController officerController,
                    Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.enquiryController = enquiryController;
        this.officerController = officerController;
        this.scanner = scanner;
        this.loginView = new LoginView(userController, scanner);
    }

    public void display() {
        boolean exit = false;

        while (!exit) {
            printWelcomeBanner();
            int choice = getMenuChoice();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    exit = confirmExit();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void printWelcomeBanner() {
        System.out.println("\n=================================");
        System.out.println("   HDB Build-To-Order (BTO) System");
        System.out.println("=================================");
        System.out.println("1. Login");
        System.out.println("2. Exit");
    }

    private int getMenuChoice() {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1 or 2)");
            }
        }
    }

    private void handleLogin() {
        try {
            User user = loginView.displayLogin();

            if (user != null) {
                redirectToRoleSpecificView(user);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private void redirectToRoleSpecificView(User user) {
        switch (user.getUserType()) {
            case "Applicant":
                ApplicantView applicantView = new ApplicantView(
                        userController,
                        projectController,
                        applicationController,
                        enquiryController,
                        scanner
                );
                applicantView.displayMenu((Applicant) user);
                break;

            case "HDBOfficer":
                HDBOfficerView officerView = new HDBOfficerView(
                        userController,
                        projectController,
                        applicationController,
                        officerController,
                        scanner
                );
                officerView.displayMenu((HDBOfficer) user);
                break;

            case "HDBManager":
                HDBManagerView managerView = new HDBManagerView(
                        userController,
                        projectController,
                        applicationController,
                        officerController,
                        enquiryController,
                        scanner
                );
                managerView.displayMenu((HDBManager) user);
                break;

            default:
                System.out.println("Unknown user type. Access denied.");
        }
    }

    private boolean confirmExit() {
        System.out.print("Are you sure you want to exit? (Y/N): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Exiting system... Goodbye!");
            return true;
        }
        return false;
    }
}