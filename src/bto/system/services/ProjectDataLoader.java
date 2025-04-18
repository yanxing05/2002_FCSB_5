package bto.system.services;

import bto.system.models.*;
import bto.system.models.users.*;
import bto.system.exceptions.FileException;

import java.util.ArrayList;
import java.util.List;

public class ProjectDataLoader {
    private final ExcelFileService excelFileService;
    private final UserService userService;

    public ProjectDataLoader(ExcelFileService excelFileService, UserService userService) {
        this.excelFileService = excelFileService;
        this.userService = userService;
    }

    public List<BTOProject> loadProjects(String filePath) throws FileException {
        List<List<String>> data = excelFileService.readExcelFile(filePath);
        List<BTOProject> projects = new ArrayList<>();

        // Skip header row
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.size() >= 13) {
                String projectName = row.get(0);
                String neighborhood = row.get(1);

                // Flat Type 1
                String type1 = row.get(2);
                int type1Units = row.get(3).isEmpty() ? 0 : Integer.parseInt(row.get(3));

                // Flat Type 2
                String type2 = row.get(5);
                int type2Units = row.get(6).isEmpty() ? 0 : Integer.parseInt(row.get(6));

                List<String> flatTypes = new ArrayList<>();
                if (type1Units > 0) flatTypes.add(type1);
                if (type2Units > 0) flatTypes.add(type2);

                // Dates
                String openingDateStr = row.get(8);
                String closingDateStr = row.get(9);

                // Manager
                String managerName = row.get(10);
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

                // Officer slots
                int officerSlots = row.get(11).isEmpty() ? 0 : Integer.parseInt(row.get(11));
                project.setOfficerSlots(officerSlots);

                // Assign officers
                String officersStr = row.get(12);
                if (officersStr != null && !officersStr.isEmpty()) {
                    String[] officerNames = officersStr.split(",");
                    for (String name : officerNames) {
                        HDBOfficer officer = findOfficerByName(name.trim());
                        if (officer != null) {
                            project.addOfficer(officer);
                            officer.setAssignedProject(project);
                        }
                    }
                }

                projects.add(project);
                manager.getManagedProjects().add(project);
            }
        }

        return projects;
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
