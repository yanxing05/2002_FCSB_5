package bto.system.services;

import bto.system.models.BTOProject;
import bto.system.models.users.HDBOfficer;
import java.util.ArrayList;
import java.util.List;
import bto.system.models.Application;

public class OfficerService {
    private List<HDBOfficer> registeredOfficers;

    public OfficerService() {
        this.registeredOfficers = new ArrayList<>();
    }

    public boolean registerOfficer(HDBOfficer officer, BTOProject project) {
        if (project.getOfficers().size() < project.getOfficerSlots()) {
            officer.setAssignedProject(project);
            registeredOfficers.add(officer);
            return true;
        }
        return false;
    }

    public boolean processFlatBooking(HDBOfficer officer, String applicationNric, String flatType) {
        // Implementation for flat booking
        for(Application app : officer.getAssignedProject().getApplications()){
            if(app.getApplicantNric().equals(applicationNric)){
                app.setStatus("Booked");
                app.setFlatType(flatType);
                officer.getAssignedProject().reduceFlatCount(flatType);
            }
        }
        return true;
    }

    public void generateBookingReceipt(HDBOfficer officer, String applicationNric) {
        // Implementation for receipt generation
        for(Application app : officer.getAssignedProject().getApplications()){
            if(app.getApplicantNric().equals(applicationNric)){
                System.out.println("Name" + app.getApplicant().getName());
                System.out.println("NRIC " + app.getApplicantNric());
                System.out.println("Age " + app.getApplicant().getAge());
                System.out.println("Marital Status " + app.getApplicant().getMaritalStatus());
                System.out.println("Flat Type " + app.getFlatType());
            }
        }
    }
}
