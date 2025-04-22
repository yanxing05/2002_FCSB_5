package bto.system.services;

import bto.system.models.*;
import bto.system.models.users.*;
import bto.system.exceptions.FileException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectDataLoader {
    private final CSVFileService csvFileService;
    private final UserService userService;
    private final OfficerService officerService;
    private final ApplicationService applicationService;
    

    public ProjectDataLoader(CSVFileService csvFileService, UserService userService, OfficerService officerService, ApplicationService applicationService) {
        this.csvFileService = csvFileService;
        this.userService = userService;
        this.officerService = officerService;
        this.applicationService = applicationService;
        
    }

    public List<BTOProject> loadProjects(String filePath) throws FileException {
        List<List<String>> data = csvFileService.readCSVFile(filePath);
        List<BTOProject> projects = new ArrayList<>();

        // Skip header row
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);

            if (row.size() >= 15) { // Updated to check for 15 columns
                // Extract fields with updated indices
                String projectId = row.get(0);
                String projectName = row.get(1);
                String neighborhood = row.get(2);

                // Flat Type 1
                String type1 = row.get(3);
                int type1Units = row.get(4).isEmpty() ? 0 : Integer.parseInt(row.get(4));
                String price1 = row.get(5); // Placeholder, if needed later

                // Flat Type 2
                String type2 = row.get(6);
                int type2Units = row.get(7).isEmpty() ? 0 : Integer.parseInt(row.get(7));
                String price2 = row.get(8); // Placeholder, if needed later

                List<String> flatTypes = new ArrayList<>();
                if (!type1.isEmpty()) flatTypes.add(type1);
                if (!type2.isEmpty()) flatTypes.add(type2);

                // Dates
                String openingDateStr = row.get(9);
                String closingDateStr = row.get(10);

                // Manager
                String managerName = row.get(11);
                HDBManager manager = findManagerByName(managerName);
                if (manager == null) {
                    System.err.println("Manager not found: " + managerName);
                    continue;
                }

                // Create project
                BTOProject project = new BTOProject(
                        projectName,
                        neighborhood,
                        flatTypes,
                        type1Units,
                        type2Units,
                        openingDateStr,
                        closingDateStr,
                        manager
                );
                project.setProjectId(projectId); // Set the Project ID
                manager.setCurrentProject(project);
                manager.getManagedProjects().add(project);

                // Officer slots
                int officerSlots = row.get(12).isEmpty() ? 0 : Integer.parseInt(row.get(12));
                project.setOfficerSlots(officerSlots);

                // Officers (index 13)
                String officersStr = row.get(13);
                if (officersStr != null && !officersStr.isEmpty()) {
                    if (officersStr.startsWith("\"") && officersStr.endsWith("\"")) {
                        officersStr = officersStr.substring(1, officersStr.length() - 1);
                    }
                    String[] officerNames = officersStr.split("\\s*,\\s*");
                    for (String name : officerNames) {
                        HDBOfficer officer = findOfficerByName(name.trim());
                        if (officer != null) {
                            project.addOfficer(officer);
                            officer.setAssignedProject(project);
                            officerService.addOfficers(officer);
                        }
                    }
                }

                // (Optional) Applications (index 14)
                // You can load application references later if needed

                projects.add(project);
            }
        }

        return projects;
    }


    public void loadApplications(String filePath, List<BTOProject> projects) throws FileException {
        List<List<String>> data = csvFileService.readCSVFile(filePath);

        // Build maps for quick lookup
        Map<String, BTOProject> projectMap = new HashMap<>();
        for (BTOProject p : projects) {
            projectMap.put(p.getProjectId(), p);
        }

        // Get all applicants from userService and map them by NRIC
        Map<String, Applicant> applicantMap = userService.getAllApplicants().stream()
                .collect(Collectors.toMap(Applicant::getNric, a -> a));

        for (int i = 1; i < data.size(); i++) { // Skipping header row
            List<String> row = data.get(i);
            if (row.size() < 6) continue; // Skip invalid rows

            // Extract fields from row
            String applicationId = row.get(0);
            String applicantNric = row.get(1);
            String status = row.get(2);
            String flatType = row.get(3);
            String applicationDateStr = row.get(4);
            String projectId = row.get(5);
            // Retrieve applicant and project by ID
            Applicant applicant = applicantMap.get(applicantNric);
            BTOProject project = projectMap.get(projectId);

            if (applicant == null || project == null) continue;

            // Create application and associate it with applicant and project
            Application application = new Application(applicant, project);
            application.setFlatType(flatType);
            application.setStatus(status);

            // Set the application ID and application date using reflection (consider refactoring if possible)
            try {
                Field idField = Application.class.getDeclaredField("applicationId");
                idField.setAccessible(true);
                idField.set(application, applicationId);

                Field dateField = Application.class.getDeclaredField("applicationDate");
                dateField.setAccessible(true);
                dateField.set(application, LocalDate.parse(applicationDateStr));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the application to the project and applicant
            project.addApplication(application);
            applicant.setApplication(application);

            // Optionally, persist application using applicationService
            applicationService.addApplication(application);
        }
    }



    private HDBManager findManagerByName(String name) {
        return userService.getAllManagers().stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private HDBOfficer findOfficerByName(String name) {
        return userService.getAllOfficers().stream()
                .filter(o -> o.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
