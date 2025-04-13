package bto.system.controllers;

import bto.system.models.BTOProject;
import bto.system.models.Enquiry;
import bto.system.models.users.User;
import bto.system.services.EnquiryService;
import java.util.ArrayList;
import java.util.List;

//public class EnquiryController {
//    private final EnquiryService enquiryService;
//
//    public EnquiryController(EnquiryService enquiryService) {
//        this.enquiryService = enquiryService;
//    }
//
//    public Enquiry createEnquiry(User user, BTOProject project, String message) {
//        return enquiryService.createEnquiry(user, project, message);
//    }
//
//    public void replyToEnquiry(Enquiry enquiry, String reply) {
//        enquiryService.replyToEnquiry(enquiry, reply);
//    }
//}

public class EnquiryController {
    private final EnquiryService enquiryService;

    public EnquiryController(EnquiryService enquiryService) {
        this.enquiryService = enquiryService;
    }

    public Enquiry createEnquiry(User user, BTOProject project, String message) {
        return enquiryService.createEnquiry(user, project, message);
    }

    public void replyToEnquiry(Enquiry enquiry, String reply) {
        enquiryService.replyToEnquiry(enquiry, reply);
    }

    public List<Enquiry> getEnquiriesForProject(BTOProject project) {
        return enquiryService.getEnquiriesForProject(project);
    }
}