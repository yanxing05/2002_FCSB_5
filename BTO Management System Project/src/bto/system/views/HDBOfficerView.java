package bto.system.views;

import bto.system.models.*;
import java.util.Scanner;
import bto.system.controllers.*;
import java.util.ArrayList;
import java.util.List;

import bto.system.models.users.Applicant;
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
            System.out.println("1. View Available Projects");
            System.out.println("2. Apply for Project");
            System.out.println("3. View My Application");
            System.out.println("4. Withdraw Application");
            System.out.println("5. View Assigned Project");
            System.out.println("6. Register for a Project");
            System.out.println("7. View Registration Status");
            System.out.println("8. Withdraw Registration");
            System.out.println("9. Submit Enquiry");
            System.out.println("10. Manage Enquiries");
            System.out.println("11. Reply Enquiries");
            System.out.println("12. Process Booking");
            System.out.println("13. Generate Receipt");
            System.out.println("14. Change Password");
            System.out.println("0. Logout");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
            	case 1:
            		viewProjects(officer);
            		break;
            	case 2:
            		applyForProject(officer);
            		break;
            	case 3:
            		viewApplication(officer);
            		break;
            	case 4:
            		withdrawApplication(officer);
            		break;
                case 5:
                    viewAssignedProject(officer);
                    break;
                case 6:
                    registerForProject(officer);
                    break;
                case 7:
                    getRegistrationStatus(officer);
                    break;
                case 8:
                	withdrawOfficerRegistration(officer);
                	break;
                case 9:
                	submitEnquiry(officer);
                	break;
                case 10:
                	manageEnquiries(officer);
                	break;
                case 11:
                    replyEnquiries(officer);
                    break;
                case 12:
                    processBooking(officer);
                    break;
                case 13:
                    generateReceipt(officer);
                    break;
                case 14:
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
                .filter(p -> {
                    // If officer, exclude the project they're assigned to
                    if (applicant instanceof HDBOfficer officer) {
                        return p.isVisible() && !p.equals(officer.getAssignedProject()) && applicant.canApplyForProject(p);
                    }
                    return p.isVisible() && applicant.canApplyForProject(p);
                })
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
    private void withdrawApplication(Applicant applicant) {
        Application app = applicationController.getApplication(applicant);
        if (app == null) {
            System.out.println("No application found.");
            return;
        }

        applicationController.withdrawApplication(applicant);
        System.out.println("Application withdrawn successfully.");
    }
    private void viewAssignedProject(HDBOfficer officer) {
        BTOProject project = officer.getAssignedProject();

        if (project != null && "Pending".equals(officer.getRegistrationStatus())) {
            System.out.println("\n=== Applied Project Details === (Pending Approval)");
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
        } 
        else if (project != null && "Approved".equals(officer.getRegistrationStatus())) {
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
        }
        else {
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
                    System.out.println("You cannot register for this project. Either you're already handling it, " +
                            "have applied for it, or the period conflicts with your current assignment.");
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
    public void replyEnquiries(HDBOfficer officer) {
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


