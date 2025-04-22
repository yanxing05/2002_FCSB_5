
package bto.system.views;

import bto.system.controllers.*;
import bto.system.exceptions.FileException;
import bto.system.services.*;
import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.users.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MainView {
    private final UserController userController;
    private final ProjectController projectController;
    private final ApplicationController applicationController;
    private final EnquiryController enquiryController;
    private final OfficerController officerController;
    private final Scanner scanner;
    private final CSVFileService csvFileService;
    private final LoginView loginView;

    public MainView(UserController userController,
                    ProjectController projectController,
                    ApplicationController applicationController,
                    EnquiryController enquiryController,
                    OfficerController officerController,
                    CSVFileService csvFileService,
                    Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.enquiryController = enquiryController;
        this.officerController = officerController;
        this.csvFileService = csvFileService;
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
                        enquiryController,
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

            // Save Officers
            List<User> officerUsers = userController.getUsersByType("hdbofficer");
            List<HDBOfficer> officers = new ArrayList<>();
            for (User user : officerUsers) {
                if (user instanceof HDBOfficer) {
                    officers.add((HDBOfficer) user);
                }
            }
            try {
                csvFileService.saveOfficersToCSV("bt/src/bto/system/data/OfficerList.csv", officers);
            } catch (FileException e) {
                System.out.println("Failed to save officer data: " + e.getMessage());
            }

            // Save Applicants
            List<User> applicantUsers = userController.getUsersByType("applicant");
            List<Applicant> applicants = new ArrayList<>();
            for (User user : applicantUsers) {
                if (user instanceof Applicant && !(user instanceof HDBOfficer)) {
                    applicants.add((Applicant) user);
                }
            }
            try {
                csvFileService.saveApplicantsToCSV("bt/src/bto/system/data/ApplicantList.csv", applicants);
            } catch (FileException e) {
                System.out.println("Failed to save applicant data: " + e.getMessage());
            }

            // Save Managers
            List<User> managerUsers = userController.getUsersByType("manager");
            List<HDBManager> managers = new ArrayList<>();
            for (User user : managerUsers) {
                if (user instanceof HDBManager) {
                    managers.add((HDBManager) user);
                }
            }
            try {
                csvFileService.saveManagersToCSV("bt/src/bto/system/data/ManagerList.csv", managers);
            } catch (FileException e) {
                System.out.println("Failed to save manager data: " + e.getMessage());
            }

            // Save Projects
            List<BTOProject> projects = projectController.getAllProjects();
            try {
                csvFileService.saveProjectsToCSV("bt/src/bto/system/data/ProjectList.csv", projects);
            } catch (FileException e) {
                System.out.println("Failed to save project data: " + e.getMessage());
            }

            // Save Applications (including officer applications)
            List<Application> applications = applicationController.getAllApplications();
            try {
                csvFileService.saveApplicationsToCSV("bt/src/bto/system/data/ApplicationList.csv", applications);
            } catch (FileException e) {
                System.out.println("Failed to save application data: " + e.getMessage());
            }

            return true;
        }
        return false;
    }


}
