package bto.system.services;

import bto.system.models.BTOProject;
import bto.system.models.users.HDBOfficer;
import java.util.ArrayList;
import java.util.List;
import bto.system.models.Application;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OfficerService {
    private List<HDBOfficer> registeredOfficers;

    public OfficerService() {
        this.registeredOfficers = new ArrayList<>();
    }

    public boolean registerOfficer(HDBOfficer officer, BTOProject project) {
        // Check if officer is eligible to register for this project
        if (!officer.canRegisterForProject(project)) {
            return false;
        }

        // Check if project has available officer slots
        if (project.getOfficers().size() >= project.getOfficerSlots()) {
            return false;
        }

        // Register officer to the project
        officer.setAssignedProject(project);
        project.addOfficer(officer);
        registeredOfficers.add(officer);
        officer.setRegistrationStatus("Pending");
        return true;
    }
    public boolean withdrawOfficerRegistration(HDBOfficer officer) {
        BTOProject assignedProject = officer.getAssignedProject();
        if (assignedProject != null) {
            assignedProject.removeOfficer(officer);
            officer.setAssignedProject(null);
            officer.setRegistrationStatus("None");
            registeredOfficers.remove(officer);
            return true;
        }
        return false;
    }

    public boolean processFlatBooking(HDBOfficer officer, String applicationNric, String flatType) {
        BTOProject project = officer.getAssignedProject();
        if (project == null) {
            System.out.println("You are not assigned to any project.");
            return false;
        }

        for (Application app : project.getApplications()) {
            if (app.getApplicantNric().equals(applicationNric)) {

                // Check if application is already booked
                if ("Booked".equals(app.getStatus())) {
                    System.out.println("This application has already been booked.");
                    return false;
                }

                // Check flat availability
                int availableCount = project.getAvailableFlatCount(flatType);
                if (availableCount <= 0) {
                    System.out.println("No more " + flatType + " flats available.");
                    return false;
                }

                // Proceed with booking
                app.setStatus("Booked");
                app.setFlatType(flatType);
                project.reduceFlatCount(flatType);

                return true;
            }
        }

        System.out.println("Application with NRIC " + applicationNric + " not found in your assigned project.");
        return false;
    }

   

public void generateBookingReceipt(HDBOfficer officer, String applicationNric) {
    BTOProject assignedProject = officer.getAssignedProject();
    if (assignedProject == null) {
        System.out.println("You are not assigned to any project.");
        return;
    }

    for (Application app : assignedProject.getApplications()) {
        if (app.getApplicantNric().equals(applicationNric) && "Booked".equalsIgnoreCase(app.getStatus())) {
            System.out.println("\n===== Booking Receipt =====");
            System.out.println("Name           : " + app.getApplicant().getName());
            System.out.println("NRIC           : " + app.getApplicantNric());
            System.out.println("Age            : " + app.getApplicant().getAge());
            System.out.println("Marital Status : " + app.getApplicant().getMaritalStatus());
            System.out.println("Flat Type      : " + app.getFlatType());
            System.out.println("Project Name   : " + assignedProject.getName());
            System.out.println("Neighborhood   : " + app.getProject().getNeighborhood());
            System.out.println("Booking Date   : " + app.getApprovalDate());
            System.out.println("============================\n");

            // Append to existing "Receipt.csv"
            try (FileWriter fw = new FileWriter("Receipt.csv", true);
                 PrintWriter pw = new PrintWriter(fw)) {

                // Append data to the CSV (assumes headers already exist)
                pw.println(String.join(",",
                        app.getApplicant().getName(),
                        app.getApplicantNric(),
                        String.valueOf(app.getApplicant().getAge()),
                        app.getApplicant().getMaritalStatus(),
                        app.getFlatType(),
                        assignedProject.getName(),
                        app.getProject().getNeighborhood(),
                        app.getApprovalDate().toString()
                ));

                System.out.println("Booking receipt saved to 'Receipt.csv'.");

            } catch (IOException e) {
                System.out.println("Error writing to CSV: " + e.getMessage());
            }

            return;
        }
    }

    System.out.println("No booked application found for NRIC: " + applicationNric);
}


}
