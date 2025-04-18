package bto.system.models;

import bto.system.models.users.User;
import java.time.LocalDateTime;

public class Enquiry {
    private String enquiryId;
    private User user;
    private BTOProject project;
    private String message;
    private String reply;
    private boolean isResolved;

    public Enquiry(User user, BTOProject project, String message) {
        this.enquiryId = "ENQ-" + System.currentTimeMillis();
        this.user = user;
        this.project = project;
        this.message = message;
        this.isResolved = false;
    }

    // Getters
    public String getEnquiryId() { return enquiryId; }
    public User getUser() { return user; }
    public BTOProject getProject() { return project; }
    public String getMessage() { return message; }
    public String getReply() { return reply; }
    public boolean isResolved() { return isResolved; }

    // Setters
    public void setReply(String reply) {
        this.reply = reply;
    }

    public void markAsResolved() {
        this.isResolved = true;
    }

    public void updateMessage(String newMessage) {
        this.message = newMessage;
    }

    @Override
    public String toString() {
        return String.format("Enquiry #%s\nProject: %s\nFrom: %s\nMessage: %s\nStatus: %s",
                enquiryId,
                project.getName(),
                user.getName(),
                message,
                isResolved ? "Resolved" : "Pending"
        );
    }
}
