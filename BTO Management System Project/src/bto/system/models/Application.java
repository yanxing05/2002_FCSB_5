package bto.system.models;

import bto.system.models.users.Applicant;
import bto.system.utils.IDGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Application {
    private String applicationId;
    private Applicant applicant;
    private BTOProject project;
    private String status; // Pending, Successful, Unsuccessful, Booked, Withdrawn
    private String flatType; // 2-Room or 3-Room
    private LocalDate applicationDate;
    private LocalDate approvalDate;

    public Application(Applicant applicant, BTOProject project) {
        this.applicationId = IDGenerator.generateApplicationID();
        this.applicant = applicant;
        this.project = project;
        this.status = "Pending";
        this.applicationDate = LocalDate.now();
        this.enquiries = new ArrayList<>();
    }

    public List<Enquiry> getEnquiries() {
        return new ArrayList<>(enquiries); // Return a copy for immutability
    }

    public void addEnquiry(Enquiry enquiry) {
        if (enquiry == null) {
            throw new IllegalArgumentException("Enquiry cannot be null");
        }
        enquiries.add(enquiry);
    }

    // Getters and setters
    public String getApplicationId() {
        return applicationId;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public BTOProject getProject() {
        return project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (status.equals("Successful") || status.equals("Unsuccessful")) {
            this.approvalDate = LocalDate.now();
        }
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public boolean isActive() {
        return !status.equals("Withdrawn") && !status.equals("Unsuccessful");
    }

    @Override
    public String toString() {
        return "Application ID: " + applicationId +
                "\nApplicant: " + applicant.getName() +
                "\nProject: " + project.getName() +
                "\nStatus: " + status +
                (flatType != null ? "\nFlat Type: " + flatType : "") +
                "\nApplication Date: " + applicationDate;
    }
}