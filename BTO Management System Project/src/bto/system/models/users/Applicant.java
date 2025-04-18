package bto.system.models.users;

import bto.system.models.Application;
import bto.system.models.BTOProject;
import bto.system.models.Enquiry;
import java.util.List;

public class Applicant extends User {
    private Application application;

    public Applicant(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String getUserType() {
        return "Applicant";
    }

    public boolean canApplyForProject(BTOProject project) {
        if (application != null && !application.getStatus().equals("Unsuccessful")) {
            return false; // Already has an active application
        }

        if (getMaritalStatus().equals("Single") && getAge() >= 35) {
            return project.getFlatTypes().stream().anyMatch(ft -> ft.getType().equals("2-Room"));
        } else if (getMaritalStatus().equals("Married") && getAge() >= 21) {
            return true;
        }
        return false;
    }

    public void withdrawApplication() {
        if (application != null) {
            application.setStatus("Withdrawn");
            application = null;
        }
    }

    public boolean canViewProject(BTOProject project) {
        return project.isVisible() || (application != null && application.getProject().equals(project));
    }

    public boolean canBookFlat() {
        return application != null && application.getStatus().equals("Successful");
    }

    public List<Enquiry> getEnquiries() {
        return application != null ? application.getEnquiries() : List.of();
    }

    public void addEnquiry(Enquiry enquiry) {
        if (application != null) {
            application.addEnquiry(enquiry);
        }
    }

    public void editEnquiry(int index, String newMessage) {
        if (application != null && index >= 0 && index < application.getEnquiries().size()) {
            application.getEnquiries().get(index).updateMessage(newMessage);
        }
    }

    public void deleteEnquiry(int index) {
        if (application != null && index >= 0 && index < application.getEnquiries().size()) {
            application.getEnquiries().remove(index);
        }
    }
}
