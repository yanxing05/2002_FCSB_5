package bto.system.models.users;

import bto.system.models.BTOProject;

public class HDBOfficer extends Applicant {
    private BTOProject assignedProject;
    private String registrationStatus;

    public HDBOfficer(String name, String nric, String password, int age, String maritalStatus) {
        super(name, nric, password, age, maritalStatus);
        this.registrationStatus = "None";
    }

    public BTOProject getAssignedProject() {
        return assignedProject;
    }

    public void setAssignedProject(BTOProject project) {
        this.assignedProject = project;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String status) {
        this.registrationStatus = status;
    }

    @Override
    public String getUserType() {
        return "HDBOfficer";
    }

    public boolean canRegisterForProject(BTOProject project) {
        // Officer already assigned to a project
        if (assignedProject != null) {
            // If the new project's opening date is before or on the same day as the current project's closing date,
            // we consider it a conflict.
            if (!project.getOpeningDate().isAfter(assignedProject.getClosingDate())) {
                return false;
            }
        }
        // Officer already applied for this project
        if (getApplication() != null && getApplication().getProject().equals(project)) {
            return false;
        }

        return true;
    }

    public void bookFlatForApplicant(Applicant applicant, String flatType) {
        if (assignedProject == null || !assignedProject.equals(applicant.getApplication().getProject())) {
            return;
        }

        if (applicant.getApplication().getStatus().equals("Successful")) {
            assignedProject.reduceFlatCount(flatType);
            applicant.getApplication().setStatus("Booked");
            applicant.getApplication().setFlatType(flatType);
        }
    }
}
