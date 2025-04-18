package bto.system.controllers;

import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.users.Applicant;
import bto.system.services.ApplicationService;

import java.util.List;

public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Application createApplication(Applicant applicant, BTOProject project) {
        return applicationService.createApplication(applicant, project);
    }

    public void withdrawApplication(Applicant applicant) {
        applicationService.withdrawApplication(applicant);
    }

    public Application getApplication(Applicant applicant) {
        return applicationService.getApplication(applicant);
    }

    public boolean hasActiveApplication(Applicant applicant) {
        return applicationService.hasActiveApplication(applicant);
    }


    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }    
}
