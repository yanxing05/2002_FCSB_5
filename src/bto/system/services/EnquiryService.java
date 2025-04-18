package bto.system.services;

import bto.system.models.Enquiry;
import bto.system.models.BTOProject;
import bto.system.models.users.User;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;

public class EnquiryService {
    private List<Enquiry> enquiries;

    public EnquiryService() {
        this.enquiries = new ArrayList<>();
    }

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
}
