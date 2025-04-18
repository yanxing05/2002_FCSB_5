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
    public boolean withdrawOfficerRegistration(HDBOfficer officer) {
        return officerService.withdrawOfficerRegistration(officer);
    }
    public boolean approveFlatBooking(HDBOfficer officer, String applicationNric, String flatType) {
        return officerService.processFlatBooking(officer, applicationNric, flatType);
    }
    public void generateReceipt(HDBOfficer officer, String applicationNric) {
        officerService.generateBookingReceipt(officer, applicationNric);
    }
}
