package bto.system.services;

import bto.system.models.users.*;
import bto.system.exceptions.FileException;
import java.util.List;
import java.util.ArrayList;

public class UserDataLoader {
    private final ExcelFileService excelFileService;

    public UserDataLoader(ExcelFileService excelFileService) {
        this.excelFileService = excelFileService;
    }

    public List<Applicant> loadApplicants(String filePath) throws FileException {
        List<List<String>> data = excelFileService.readExcelFile(filePath);
        List<Applicant> applicants = new ArrayList<>();

        // Skip header row
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.size() >= 5) {
                String name = row.get(0);
                String nric = row.get(1);
                int age = Integer.parseInt(row.get(2));
                String maritalStatus = row.get(3);
                String password = row.get(4);

                applicants.add(new Applicant(name, nric, password, age, maritalStatus));
            }
        }

        return applicants;
    }

    public List<HDBOfficer> loadOfficers(String filePath) throws FileException {
        List<List<String>> data = excelFileService.readExcelFile(filePath);
        List<HDBOfficer> officers = new ArrayList<>();

        // Skip header row
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.size() >= 5) {
                String name = row.get(0);
                String nric = row.get(1);
                int age = Integer.parseInt(row.get(2));
                String maritalStatus = row.get(3);
                String password = row.get(4);

                officers.add(new HDBOfficer(name, nric, password, age, maritalStatus));
            }
        }

        return officers;
    }

    public List<HDBManager> loadManagers(String filePath) throws FileException {
        List<List<String>> data = excelFileService.readExcelFile(filePath);
        List<HDBManager> managers = new ArrayList<>();

        // Skip header row
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            if (row.size() >= 5) {
                String name = row.get(0);
                String nric = row.get(1);
                int age = Integer.parseInt(row.get(2));
                String maritalStatus = row.get(3);
                String password = row.get(4);

                managers.add(new HDBManager(name, nric, password, age, maritalStatus));
            }
        }

        return managers;
    }
}
