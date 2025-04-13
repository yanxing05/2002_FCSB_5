package bto.system.services;

import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.users.Applicant;
import java.util.ArrayList;
import java.util.List;

public class ApplicationService {
    private List<Application> applications;

    public ApplicationService() {
        this.applications = new ArrayList<>();
    }

    public Application createApplication(Applicant applicant, BTOProject project) {
        Application application = new Application(applicant, project);
        applications.add(application);
        return application;
    }

    public void withdrawApplication(Application application) {
        application.setStatus("Withdrawn");
    }
}
