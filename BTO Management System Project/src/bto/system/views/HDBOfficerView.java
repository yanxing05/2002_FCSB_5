package bto.system.views;

import bto.system.models.*;
import java.util.Scanner;
import bto.system.controllers.*;
import java.util.ArrayList;
import java.util.List;
import bto.system.models.users.HDBOfficer;

public class HDBOfficerView {
    private final UserController userController;
    private final ProjectController projectController;
    private final ApplicationController applicationController;
    private final OfficerController officerController;
    private final EnquiryController enquiryController;
    private Scanner scanner;

    public HDBOfficerView(UserController userController,
                          ProjectController projectController,
                          ApplicationController applicationController,
                          OfficerController officerController,
                          EnquiryController enquiryController,
                          Scanner scanner) {
        this.userController = userController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.officerController = officerController;
        this.enquiryController = enquiryController;
        this.scanner = scanner;
    }

    public void displayMenu(HDBOfficer officer) {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n=== HDB Officer Menu ===");
            System.out.println("1. View Assigned Project");
            System.out.println("2. Register for a Project");
            System.out.println("3. View Registration Status");
            System.out.println("4. Withdraw Registration");
            System.out.println("5. View enquiries");
            System.out.println("6. Process Booking");
            System.out.println("7. Generate Receipt");
            System.out.println("8. Change Password");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAssignedProject(officer);
                    break;
                case 2:
                    registerForProject(officer);
                    break;
                case 3:
                    getRegistrationStatus(officer);
                    break;
                case 4:
                	withdrawOfficerRegistration(officer);
                	break;
                case 5:
                    viewEnquiries(officer);
                    break;
                case 6:
                    processBooking(officer);
                    break;
                case 7:
                    generateReceipt(officer);
                    break;
                case 8:
                    if (changePassword(officer)) {
                        System.out.println("Password changed successfully. Please login again.");
                        logout = true;
                    } else {
                        System.out.println("Password change failed.");
                    }
                    break;
                case 0:
                    logout = true;
                    break;
            }
        }
    }

    private void viewAssignedProject(HDBOfficer officer) {
        BTOProject project = officer.getAssignedProject();

        if (project != null) {
            System.out.println("\n=== Assigned Project Details ===");
            System.out.println("Project ID       : " + project.getProjectId());
            System.out.println("Name             : " + project.getName());
            System.out.println("Neighborhood     : " + project.getNeighborhood());
            System.out.println("Opening Date     : " + project.getOpeningDate());
            System.out.println("Closing Date     : " + project.getClosingDate());
            System.out.println("--- Available Flats ---");
            for (FlatType ft : project.getFlatTypes()) {
                System.out.println(ft.getType() + " : " + project.getAvailableFlatCount(ft.getType()));
            }
            System.out.println("===============================\n");
        } else {
            System.out.println("No project assigned yet.");
        }
    }


    public void registerForProject(HDBOfficer officer) {
        List<BTOProject> projects = projectController.getAllProjects();

        if (projects.isEmpty()) {
            System.out.println("No BTO projects available for registration at the moment.");
            return;
        }

        System.out.println("\n=== Available BTO Projects ===");
        for (int i = 0; i < projects.size(); i++) {
            BTOProject project = projects.get(i);
            System.out.println((i + 1) + ". " + project.getName() + " - " + project.getNeighborhood() +
                    " (Opens: " + project.getOpeningDate() + ", Closes: " + project.getClosingDate() + ")");
        }

        System.out.print("Enter the project number to register: ");
        try {
            int projectIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (projectIndex >= 0 && projectIndex < projects.size()) {
                BTOProject selectedProject = projects.get(projectIndex);
     
                if (!officer.canRegisterForProject(selectedProject)) {
                    System.out.println("You are either already assigned to this project or registration conflicts with your current assignment.");
                    return;
                }

                officerController.registerForProject(officer, selectedProject);
                System.out.println("You have successfully registered for project: " + selectedProject.getName());
            } else {
                System.out.println("Invalid project number. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    public void getRegistrationStatus(HDBOfficer officer){
        System.out.println("Registration Status: " + officer.getRegistrationStatus());
    }
    public void withdrawOfficerRegistration(HDBOfficer officer) {
    	if (officer.getAssignedProject() == null) {
            System.out.println("You are not currently registered to any project.");
        } else {
            boolean success = officerController.withdrawOfficerRegistration(officer);
            if (success) {
                System.out.println("You have successfully withdrawn from the project.");
            } else {
                System.out.println("Withdrawal failed.");
            }
        }
    }
    public void viewEnquiries(HDBOfficer officer) {
        BTOProject assignedProject = officer.getAssignedProject();

        if (assignedProject == null) {
            System.out.println("You are not assigned to any project.");
            return;
        }

        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(assignedProject);

        if (enquiries.isEmpty()) {
            System.out.println("No enquiries found for your assigned project.");
            return;
        }

        System.out.println("\n=== Enquiries for Project: " + assignedProject.getName() + " ===");
        for (Enquiry enquiry : enquiries) {
            System.out.println("[" + enquiry.getEnquiryId() + "] " + enquiry.getMessage());
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\nChoose an action:");
            System.out.println("1. Reply to an enquiry");
            System.out.println("2. Exit");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Enquiry ID: ");
                    String enquiryId = scanner.nextLine();

                    Enquiry selectedEnquiry = enquiries.stream()
                            .filter(e -> e.getEnquiryId().equals(enquiryId))
                            .findFirst()
                            .orElse(null);

                    if (selectedEnquiry != null) {
                        System.out.print("Enter your reply: ");
                        String reply = scanner.nextLine();
                        enquiryController.replyToEnquiry(selectedEnquiry, reply);
                        System.out.println("Reply sent.");
                    } else {
                        System.out.println("Enquiry ID not found. Please try again.");
                    }
                    break;

                case 2:
                    exit = true;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public void processBooking(HDBOfficer officer) {
        BTOProject officerProject = officer.getAssignedProject();

        if (officerProject == null) {
            System.out.println("You are not assigned to any project.");
            return;
        }

        List<Application> eligibleApplications = officerProject.getApplications().stream()
            .filter(app -> "Successful".equals(app.getStatus()))
            .toList();

        if (eligibleApplications.isEmpty()) {
            System.out.println("No successful applications available for booking.");
            return;
        }

        System.out.println("\n=== Successful Applicants for Project: " + officerProject.getName() + " ===");
        for (Application app : eligibleApplications) {
            System.out.println("- " + app.getApplicantNric());
        }

        System.out.print("Enter Applicant's NRIC to book a flat: ");
        String enteredNric = scanner.nextLine();

        Application selectedApp = eligibleApplications.stream()
            .filter(app -> app.getApplicantNric().equals(enteredNric))
            .findFirst()
            .orElse(null);

        if (selectedApp == null) {
            System.out.println("Invalid NRIC or application not eligible.");
            return;
        }

        System.out.println("\n=== Applicant's BTO Application Details ===");
        System.out.println("Name: " + selectedApp.getApplicant().getName());
        System.out.println("NRIC: " + selectedApp.getApplicantNric());
        System.out.println("Marital Status: " + selectedApp.getApplicant().getMaritalStatus());
        System.out.println("Project: " + selectedApp.getProject().getName());
        System.out.println("Status: " + selectedApp.getStatus());
        System.out.println("Flat Type: " + selectedApp.getFlatType());
        System.out.println("-----------------------------------------");

        officerController.approveFlatBooking(officer, enteredNric, selectedApp.getFlatType());
        System.out.println("✅ Flat booking successful!");
    }

    public void generateReceipt(HDBOfficer officer) {
        BTOProject assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println("You are not assigned to any project.");
            return;
        }

        List<Application> bookedApplications = new ArrayList<>();
        for (Application app : assignedProject.getApplications()) {
            if ("Booked".equalsIgnoreCase(app.getStatus())) {
                bookedApplications.add(app);
            }
        }

        if (bookedApplications.isEmpty()) {
            System.out.println("There are no booked applications for your project.");
            return;
        }

        System.out.println("=== Booked Applicants ===");
        for (Application app : bookedApplications) {
            System.out.println("- " + app.getApplicantNric() + " (" + app.getApplicant().getName() + ")");
        }

        System.out.print("\nEnter the NRIC of the applicant to generate receipt: ");
        String enteredNric = scanner.nextLine();

        boolean found = false;
        for (Application app : bookedApplications) {
            if (app.getApplicantNric().equals(enteredNric)) {
                officerController.generateReceipt(officer, enteredNric);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Invalid NRIC. No matching booked application found.");
        }
    }

    private boolean changePassword(HDBOfficer officer) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter new password (min 8 characters): ");
        String newPassword = scanner.nextLine();

        try {
            officer.changePassword(oldPassword, newPassword);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

}


