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
            System.out.println("4. View enquiries");
            System.out.println("5. Process Booking");
            System.out.println("6. Generate Receipt");
            System.out.println("7. Change Password");
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
                    viewEnquiries(officer);
                    break;
                case 5:
                    processBooking(officer);
                    break;
                case 6:
                    generateReceipt(officer);
                    break;
                case 7:
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
        if (officer.getAssignedProject() != null) {
            System.out.println(officer.getAssignedProject().getName());
        }
    }
    public void registerForProject(HDBOfficer officer){
        System.out.println("Choose the project to register: ");
        List<BTOProject> projects = projectController.getAllProjects();
        for(BTOProject project : projects) {
        	System.out.println("- " + project.getName());
        }
        int project_index = scanner.nextInt();
        officerController.registerForProject(officer, projects.get(project_index));
    }
    public void getRegistrationStatus(HDBOfficer officer){
        System.out.println(officer.getRegistrationStatus());
    }
    public void viewEnquiries(HDBOfficer officer){
        List<Enquiry> enquiries = enquiryController.getEnquiriesForProject(officer.getAssignedProject());
        for(Enquiry enquiry : enquiries){
            System.out.println(enquiry.getEnquiryId() + ": " + enquiry.getMessage());
        }
        boolean exit = false;
        while(!exit){
            System.out.println("Choose a choice: ");
            System.out.println("-----------------");
            System.out.println("1. Reply");
            System.out.println("2. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch(choice){
                case 1:
                    System.out.println("Enter enquiry ID: ");
                    String idInput = scanner.nextLine();
                    
                    for(Enquiry enq : enquiries){
                        if(idInput.equals(enq.getEnquiryId())){
                            System.out.print("Write a reply: ");
                            String reply = scanner.nextLine();
                            scanner.nextLine();
                            enquiryController.replyToEnquiry(enq, reply);
                        }
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
    public void processBooking(HDBOfficer officer){
        System.out.println("Enter Applicant's NRIC to Book Flat: ");
        System.out.println("----------------------------");
        BTOProject officerProject = officer.getAssignedProject();
        List<Application> applications = officerProject.getApplications();
        for(Application app : applications){
            System.out.println(app.getApplicant().getNric());
        }
        String enteredNric = scanner.nextLine();
        for(Application app : applications){
            if(app.getApplicant().getNric().equals(enteredNric)){
                System.out.println("Applicant's BTO Application:");
                System.out.println("Project ID: " + app.getProject().getProjectId());
                System.out.println("Status: " + app.getStatus());
                System.out.println("Flat Type: " + app.getFlatType());
                System.out.println("----------------------------");
                officerController.approveFlatBooking(officer, enteredNric, app.getFlatType());
                System.out.println("Flat booking successful!");
            }
        }

    }
    public void generateReceipt(HDBOfficer officer){
        System.out.println("Enter Applicant's NRIC to generate receipt: ");
        for(Application app : officer.getAssignedProject().getApplications()){
            if(app.getStatus().equals("Booked"))
            System.out.println(app.getApplicantNric());
        }
        String enteredNric = scanner.nextLine();
        officerController.generateReceipt(officer, enteredNric);
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




