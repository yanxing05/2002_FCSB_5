package bto.system.services;

import bto.system.models.Enquiry;
import bto.system.models.BTOProject;
import bto.system.models.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnquiryService {
    private List<Enquiry> enquiries;

    public EnquiryService() {
        this.enquiries = new ArrayList<>();
    }

    // ✅ Existing methods

    public Enquiry createEnquiry(User user, BTOProject project, String message) {
        Enquiry enquiry = new Enquiry(user, project, message);
        enquiries.add(enquiry);
        return enquiry;
    }

    public void replyToEnquiry(Enquiry enquiry, String reply) {
        enquiry.setReply(reply);
        enquiry.markAsResolved();
    }

    public List<Enquiry> getEnquiriesForProject(BTOProject project) {
        return enquiries.stream()
                .filter(e -> e.getProject().equals(project))
                .collect(Collectors.toList());
    }

    
    public List<Enquiry> getEnquiriesByUser(User user) {
        return enquiries.stream()
                .filter(e -> e.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public boolean editEnquiry(User user, String enquiryId, String newMessage) {
        for (Enquiry e : enquiries) {
            if (e.getEnquiryId().equals(enquiryId) && e.getUser().equals(user)) {
                e.updateMessage(newMessage);
                return true;
            }
        }
        return false;
    }

    public boolean deleteEnquiry(User user, String enquiryId) {
        return enquiries.removeIf(e ->
                e.getEnquiryId().equals(enquiryId) &&
                e.getUser().equals(user) &&
                !e.isResolved()
        );
    }

    public List<Enquiry> getAllEnquiries() {
        return new ArrayList<>(enquiries);
    }
}
