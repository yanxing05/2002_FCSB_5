package bto.system.views;

import bto.system.controllers.ApplicationController;
import bto.system.controllers.ProjectController;
import bto.system.controllers.UserController;
import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.Enquiry;
import bto.system.models.users.Applicant;

import java.util.List;
import java.util.Scanner;

public class ApplicantView {
    private final UserController userController;
    private final ProjectController projectController;
    private final ApplicationController applicationController;
    private final Scanner scanner;

    public ApplicantView(UserController userController,
                         ProjectController projectController,
                         ApplicationController applicationController,
                         Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.scanner = scanner;
    }

    public void displayMenu(Applicant applicant) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== Applicant Menu ===");
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for Project");
            System.out.println("3. View My Application");
            System.out.println("4. Submit Enquiry");
            System.out.println("5. View/Edit/Delete Enquiries");
            System.out.println("6. Withdraw Application");
            System.out.println("7. Change Password");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewProjects(applicant);
                case 2 -> applyForProject(applicant);
                case 3 -> viewApplication(applicant);
                case 4 -> submitEnquiry(applicant);
                case 5 -> manageEnquiries(applicant);
                case 6 -> withdrawApplication(applicant);
                case 7->{
                    if (changePassword(applicant)) {
                        System.out.println("Password changed successfully. Please login again.");
                        logout = true;
                    } else {
                        System.out.println("Password change failed.");
                    }
                    break;}
                case 0 -> logout = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewProjects(Applicant applicant) {
        System.out.println("\n--- Available Projects ---");
        List<BTOProject> available = projectController.getAllProjects().stream()
                .filter(p -> p.isVisible() && applicant.canApplyForProject(p))
                .toList();

        if (available.isEmpty()) {
            System.out.println("No eligible projects available.");
        } else {
            available.forEach(p -> System.out.println("- " + p.getName()));
        }
    }

    private void applyForProject(Applicant applicant) {
        if (applicationController.hasActiveApplication(applicant)) {
            System.out.println("You already have an active application.");
            return;
        }

        List<BTOProject> available = projectController.getAllProjects().stream()
                .filter(p -> p.isVisible() && applicant.canApplyForProject(p))
                .toList();

        if (available.isEmpty()) {
            System.out.println("No eligible projects available.");
            return;
        }

        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ". " + available.get(i).getName());
        }

        System.out.print("Choose a project to apply (number): ");
        int choice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (choice < 0 || choice >= available.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        BTOProject selected = available.get(choice);
        applicationController.createApplication(applicant, selected);
        System.out.println("Application submitted successfully.");
    }

    private void viewApplication(Applicant applicant) {
        Application app = applicationController.getApplication(applicant);
        if (app == null) {
            System.out.println("No application found.");
            return;
        }

        System.out.println("\n--- Your Application ---");
        System.out.println(app);

        if (app.getStatus().equals("Successful")) {
            System.out.println("You may proceed to book a flat with an HDB Officer.");
        } else if (app.getStatus().equals("Booked")) {
            System.out.println("You have successfully booked a flat.");
        }
    }

    private void submitEnquiry(Applicant applicant) {
        Application app = applicationController.getApplication(applicant);
        if (app == null) {
            System.out.println("You must apply for a project before submitting enquiries.");
            return;
        }

        System.out.print("Enter your enquiry message: ");
        String msg = scanner.nextLine();

        Enquiry enquiry = new Enquiry(applicant, app.getProject(), msg);
        applicationController.addEnquiry(applicant, enquiry);

        System.out.println("Enquiry submitted.");
    }

    private void manageEnquiries(Applicant applicant) {
        List<Enquiry> enquiries = applicationController.getEnquiries(applicant);
        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found.");
            return;
        }

        for (int i = 0; i < enquiries.size(); i++) {
            System.out.println((i + 1) + ". " + enquiries.get(i));
        }

        System.out.print("Choose an enquiry to manage (or 0 to go back): ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index < 0 || index >= enquiries.size()) return;
        Enquiry selected = enquiries.get(index);

        System.out.println("1. Edit Message");
        System.out.println("2. Delete Enquiry");
        System.out.println("0. Cancel");
        System.out.print("Choice: ");

        int action = scanner.nextInt();
        scanner.nextLine();

        switch (action) {
            case 1 -> {
                System.out.print("Enter new message: ");
                String newMsg = scanner.nextLine();
                selected.updateMessage(newMsg);
                System.out.println("Message updated.");
            }
            case 2 -> {
                applicationController.deleteEnquiry(applicant, selected);
                System.out.println("Enquiry deleted.");
            }
            default -> System.out.println("Action cancelled.");
        }
    }

    private void withdrawApplication(Applicant applicant) {
        Application app = applicationController.getApplication(applicant);
        if (app == null) {
            System.out.println("No application found.");
            return;
        }

        applicationController.withdrawApplication(applicant);
        System.out.println("Application withdrawn successfully.");
    }
    private boolean changePassword(Applicant applicant) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter new password (min 8 characters): ");
        String newPassword = scanner.nextLine();

        try {
            applicant.changePassword(oldPassword, newPassword);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
