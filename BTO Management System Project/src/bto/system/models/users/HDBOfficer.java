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
    // Officer already assigned to this project — no need to register again
    if (assignedProject != null && assignedProject.equals(project)) {
        return false;
    }

    // Conflict: new project overlaps with currently assigned project
    if (assignedProject != null && !project.getOpeningDate().isAfter(assignedProject.getClosingDate())) {
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
