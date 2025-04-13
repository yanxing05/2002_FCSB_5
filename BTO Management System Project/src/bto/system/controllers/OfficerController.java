package bto.system.controllers;

import bto.system.models.BTOProject;
import bto.system.models.users.HDBOfficer;
import bto.system.services.OfficerService;

public class OfficerController {
    private final OfficerService officerService;

    public OfficerController(OfficerService officerService) {
        this.officerService = officerService;
    }

    public boolean registerForProject(HDBOfficer officer, BTOProject project) {
        return officerService.registerOfficer(officer, project);
    }

    public boolean approveFlatBooking(HDBOfficer officer, String applicationId, String flatType) {
        return officerService.processFlatBooking(officer, applicationId, flatType);
    }

    public String generateReceipt(HDBOfficer officer, String applicationId) {
        return officerService.generateBookingReceipt(officer, applicationId);
    }
}