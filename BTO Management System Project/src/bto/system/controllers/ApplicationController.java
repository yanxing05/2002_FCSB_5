package bto.system.controllers;

import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.users.Applicant;
import bto.system.services.ApplicationService;

public class ApplicationController {
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public Application createApplication(Applicant applicant, BTOProject project) {
        return applicationService.createApplication(applicant, project);
    }

    public void withdrawApplication(Application application) {
        applicationService.withdrawApplication(application);
    }
}