package bto.system.controllers;

import bto.system.models.Enquiry;
import bto.system.models.BTOProject;
import bto.system.models.users.User;
import bto.system.services.EnquiryService;

import java.util.List;

public class EnquiryController {
    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    public Enquiry submitEnquiry(User user, BTOProject project, String message) {
        return enquiryService.createEnquiry(user, project, message);
    }



    public boolean editEnquiry(User user, String enquiryId, String newMessage) {
        return enquiryService.editEnquiry(user, enquiryId, newMessage);
    }

    public boolean deleteEnquiry(User user, String enquiryId) {
        return enquiryService.deleteEnquiry(user, enquiryId);
    }
    public List<Enquiry> getUserEnquiries(User user) {
            return enquiryService.getEnquiriesByUser(user);
        }
    public List<Enquiry> getAllEnquiries() {
        return enquiryService.getAllEnquiries();
    }

    public List<Enquiry> getEnquiriesForProject(BTOProject project) {
        return enquiryService.getEnquiriesForProject(project);
    }
    public void replyToEnquiry(Enquiry enquiry, String reply) {
        enquiryService.replyToEnquiry(enquiry, reply);
    }
}
