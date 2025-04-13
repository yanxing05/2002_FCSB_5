package bto.system.services;

import bto.system.models.BTOProject;
import bto.system.models.users.HDBOfficer;
import java.util.ArrayList;
import java.util.List;

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

    public boolean processFlatBooking(HDBOfficer officer, String applicationId, String flatType) {
        // Implementation for flat booking
        return true;
    }

    public String generateBookingReceipt(HDBOfficer officer, String applicationId) {
        // Implementation for receipt generation
        return "Receipt for application: " + applicationId;
    }
}