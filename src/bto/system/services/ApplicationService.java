package bto.system.services;

import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.Enquiry;
import bto.system.models.users.Applicant;

import java.util.ArrayList;
import java.util.List;

public class ApplicationService {
    private final List<Application> applications;

    public ApplicationService() {
        this.applications = new ArrayList<>();
    }

    public Application createApplication(Applicant applicant, BTOProject project) {
        Application application = new Application(applicant, project);
        applications.add(application);
        applicant.setApplication(application);
        return application;
    }

    public void withdrawApplication(Applicant applicant) {
        Application app = applicant.getApplication();
        if (app != null && app.isActive()) {
            app.setStatus("Pending Withdrawal");
            System.out.println("Your withdrawal request has been submitted and is pending approval.");
        } else {
            System.out.println("No active application to withdraw.");
        }
    }

    public Application getApplication(Applicant applicant) {
        return applicant.getApplication();
    }

    public boolean hasActiveApplication(Applicant applicant) {
        Application app = applicant.getApplication();
        return app != null && app.isActive();
    }

    public void addEnquiry(Applicant applicant, Enquiry enquiry) {
        Application app = applicant.getApplication();
        if (app != null) {
            app.addEnquiry(enquiry);
        }
    }

    public void deleteEnquiry(Applicant applicant, Enquiry enquiry) {
        Application app = applicant.getApplication();
        if (app != null) {
            app.getEnquiries().remove(enquiry); // you could improve this with a custom method
        }
    }

    public List<Enquiry> getEnquiries(Applicant applicant) {
        Application app = applicant.getApplication();
        return app != null ? app.getEnquiries() : new ArrayList<>();
    }

    public List<Application> getAllApplications() {
        return applications;
    }
}
