package bto.system.models;

import bto.system.models.users.HDBManager;
import bto.system.utils.DateUtils;
import bto.system.utils.IDGenerator;
import bto.system.models.users.HDBOfficer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class BTOProject {
    private String projectId;
    private String name;
    private String neighborhood;
    private List<FlatType> flatTypes;
    private LocalDate openingDate;
    private LocalDate closingDate;
    private HDBManager manager;
    private List<HDBOfficer> officers;
    private boolean visible;
    private int officerSlots;
    private List<Application> applications;

    public BTOProject(String name, String neighborhood, List<String> flatTypeNames,
                      int twoRoomCount, int threeRoomCount,
                      String openingDateStr, String closingDateStr, HDBManager manager) {
        this.projectId = IDGenerator.generateProjectID();
        this.name = name;
        this.neighborhood = neighborhood;
        this.flatTypes = new ArrayList<>();

        for (String type : flatTypeNames) {
            if (type.equals("2-Room")) {
                flatTypes.add(new FlatType(type, twoRoomCount));
            } else if (type.equals("3-Room")) {
                flatTypes.add(new FlatType(type, threeRoomCount));
            }
        }

        this.openingDate = DateUtils.parseDate(openingDateStr.trim());
        this.closingDate = DateUtils.parseDate(closingDateStr.trim());

        this.manager = manager;
        this.officers = new ArrayList<>();
        this.applications = new ArrayList<>();
        this.visible = false;
        this.officerSlots = 10;
    }

    public void setTwoRoomCount(int count) {
        if (count < 0) throw new IllegalArgumentException("Count cannot be negative");
        for (FlatType ft : flatTypes) {
            if ("2-Room".equals(ft.getType())) {
                ft.setAvailableUnits(count);
                break;
            }
        }
    }

    public void setThreeRoomCount(int count) {
        if (count < 0) throw new IllegalArgumentException("Count cannot be negative");
        for (FlatType ft : flatTypes) {
            if ("3-Room".equals(ft.getType())) {
                ft.setAvailableUnits(count);
                break;
            }
        }
    }

    public int getTwoRoomCount() {
        for (FlatType ft : flatTypes) {
            if ("2-Room".equals(ft.getType())) {
                return ft.getAvailableUnits();
            }
        }
        return 0;
    }

    public int getThreeRoomCount() {
        for (FlatType ft : flatTypes) {
            if ("3-Room".equals(ft.getType())) {
                return ft.getAvailableUnits();
            }
        }
        return 0;
    }

    public List<Enquiry> getEnquiries() {
        List<Enquiry> allEnquiries = new ArrayList<>();
        for (Application app : applications) {
            allEnquiries.addAll(app.getEnquiries());
        }
        return allEnquiries;
    }

    // Add this method for direct enquiry addition
    public void addEnquiryToApplication(String applicationId, Enquiry enquiry) {
        for (Application app : applications) {
            if (app.getApplicationId().equals(applicationId)) {
                app.addEnquiry(enquiry);
                return;
            }
        }
        throw new IllegalArgumentException("Application not found with ID: " + applicationId);
    }

    // Getters and setters
    public String getProjectId() { return projectId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }
    public List<FlatType> getFlatTypes() { return flatTypes; }
    public void setFlatTypes(List<String> flatTypeNames) {
        this.flatTypes = new ArrayList<>();
        for (String type : flatTypeNames) {
            if (type.equals("2-Room")) {
                flatTypes.add(new FlatType(type, 0));
            } else if (type.equals("3-Room")) {
                flatTypes.add(new FlatType(type, 0));
            }
        }
    }
    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.openingDate = LocalDate.parse(dateStr.trim(), formatter);
    }
    public LocalDate getClosingDate() { return closingDate; }
    public void setClosingDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.closingDate = LocalDate.parse(dateStr.trim(), formatter);
    }
    public HDBManager getManager() { return manager; }
    public List<HDBOfficer> getOfficers() { return officers; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public int getOfficerSlots() { return officerSlots; }
    public void setOfficerSlots(int slots) { this.officerSlots = slots; }
    public List<Application> getApplications() { return applications; }

    public void addOfficer(HDBOfficer officer) {
        if (officers.size() < officerSlots) {
            officers.add(officer);
        }
    }

    public void removeOfficer(HDBOfficer officer) {
        officers.remove(officer);
    }

    public void addApplication(Application application) {
        applications.add(application);
    }

    public void reduceFlatCount(String flatType) {
        for (FlatType ft : flatTypes) {
            if (ft.getType().equals(flatType)) {
                ft.reduceAvailableUnits();
                break;
            }
        }
    }

    public boolean isAcceptingApplications() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(openingDate) && !today.isAfter(closingDate);
    }

    public boolean hasAvailableFlats(String flatType) {
        for (FlatType ft : flatTypes) {
            if (ft.getType().equals(flatType) && ft.getAvailableUnits() > 0) {
                return true;
            }
        }
        return false;
    }

    public int getAvailableFlatCount(String flatType) {
        for (FlatType ft : flatTypes) {
            if (ft.getType().equals(flatType)) {
                return ft.getAvailableUnits();
            }
        }
        return 0;
    }
}
