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
            System.out.println("8. Submit Enquiry");
            System.out.println("9. Manage My Enquiries");
            System.out.println("10. Reply Enquiries");
            System.out.println("11. Process Booking");
            System.out.println("12. Generate Receipt");
            System.out.println("13. Change Password");
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
                	submitEnquiry(officer);
                	break;
                case 9:
                	manageEnquiries(officer);
                	break;
                case 10:
                    replyEnquiries(officer);
                    break;
                case 11:
                    processBooking(officer);
                    break;
                case 12:
                    generateReceipt(officer);
                    break;
                case 13:
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

        List<FlatType> flatTypes = selected.getFlatTypes();
        List<FlatType> eligibleFlatTypes = flatTypes.stream()
                .filter(ft -> ft.getAvailableUnits() > 0 && applicant.isEligibleForFlatType(ft.getType()))
                .toList();

        if (eligibleFlatTypes.isEmpty()) {
            System.out.println("No flat types available that you are eligible for.");
            return;
        }

        System.out.println("Available Flat Types (You are eligible for):");
        for (int i = 0; i < eligibleFlatTypes.size(); i++) {
            FlatType ft = eligibleFlatTypes.get(i);
            System.out.println((i + 1) + ". " + ft.getType() + " (" + ft.getAvailableUnits() + " units)");
        }

        System.out.print("Choose a flat type: ");
        int flatChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (flatChoice < 0 || flatChoice >= eligibleFlatTypes.size()) {
            System.out.println("Invalid flat type selection.");
            return;
        }

        String chosenFlatType = eligibleFlatTypes.get(flatChoice).getType();
        applicationController.createApplication(applicant, selected, chosenFlatType);
        System.out.println("Application submitted successfully for " + chosenFlatType + ".");
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
        String status = officer.getRegistrationStatus();

        if (project != null && ("Pending".equalsIgnoreCase(status) || "Approved".equalsIgnoreCase(status))) {
            System.out.println("\n=== " + (status.equalsIgnoreCase("Pending") ? "Applied" : "Assigned") + " Project Details ===" + 
                               (status.equalsIgnoreCase("Pending") ? " (Pending Approval)" : ""));
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
    
    private void submitEnquiry(Applicant applicant) {
        List<BTOProject> availableProjects = projectController.getAllProjects().stream()
                .filter(p -> p.isVisible())
                .toList();
    
        if (availableProjects.isEmpty()) {
            System.out.println("No eligible projects to submit enquiry.");
            return;
        }
    
        for (int i = 0; i < availableProjects.size(); i++) {
            System.out.println((i + 1) + ". " + availableProjects.get(i).getName());
        }
    
        System.out.print("Choose a project to submit an enquiry for: ");
        int choice = scanner.nextInt() - 1;
        scanner.nextLine();
    
        if (choice < 0 || choice >= availableProjects.size()) {
            System.out.println("Invalid choice.");
            return;
        }
    
        BTOProject selectedProject = availableProjects.get(choice);
        System.out.print("Enter your enquiry message: ");
        String msg = scanner.nextLine();
    
        Enquiry enquiry = new Enquiry(applicant, selectedProject, msg);
        enquiryController.submitEnquiry(applicant, selectedProject, msg);
    
        System.out.println("Enquiry submitted.");
    }
    
    private void manageEnquiries(Applicant applicant) {
        List<Enquiry> enquiries = enquiryController.getUserEnquiries(applicant);
    
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
                enquiryController.editEnquiry(applicant, selected.getEnquiryId(), newMsg);
                System.out.println("Message updated.");
            }
            case 2 -> {
                enquiryController.deleteEnquiry(applicant, selected.getEnquiryId());
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

        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(assignedProject).stream()
                .filter(e -> !e.isResolved())
                .toList();

        if (enquiries.isEmpty()) {
            System.out.println("No unresolved enquiries found for your assigned project.");
            return;
        }

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Unresolved Enquiries for Project: " + assignedProject.getName() + " ===");
            List<Enquiry> active_enquiries = enquiryController.getEnquiriesForProject(assignedProject).stream()
                    .filter(e -> !e.isResolved())
                    .toList();
            for (int i = 0; i < active_enquiries.size(); i++) {
                Enquiry enquiry = active_enquiries.get(i);
                System.out.println((i + 1) + ". [" + enquiry.getEnquiryId() + "] " + enquiry.getMessage());
            }

            System.out.println("\nChoose an action:");
            System.out.println("1. Reply to an enquiry");
            System.out.println("2. Exit");
            System.out.print("Choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter the number of the enquiry you want to reply to: ");
                    int enquiryIndex;
                    try {
                        enquiryIndex = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        continue;
                    }

                    if (enquiryIndex < 1 || enquiryIndex > active_enquiries.size()) {
                        System.out.println("Invalid number. Please select a valid enquiry.");
                        continue;
                    }

                    Enquiry selectedEnquiry = active_enquiries.get(enquiryIndex - 1);
                    System.out.print("Enter your reply: ");
                    String reply = scanner.nextLine();
                    enquiryController.replyToEnquiry(selectedEnquiry, reply);
                    System.out.println("Reply sent.");
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


