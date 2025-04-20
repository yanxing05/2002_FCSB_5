package bto.system.services;

import bto.system.exceptions.FileException;

import bto.system.models.users.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import bto.system.models.BTOProject;
import bto.system.models.FlatType;
public class CSVFileService {

    // Reads CSV file and returns data as List of rows (List<String>)
	public List<List<String>> readCSVFile(String filePath) throws FileException {
	    List<List<String>> data = new ArrayList<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = br.readLine()) != null) {
	            List<String> rowData = parseCSVLine(line);
	            data.add(rowData);
	        }
	    } catch (IOException e) {
	        throw new FileException("Error reading CSV file: " + filePath);
	    }

	    return data;
	}
	private List<String> parseCSVLine(String line) {
	    List<String> result = new ArrayList<>();
	    StringBuilder current = new StringBuilder();
	    boolean inQuotes = false;

	    for (int i = 0; i < line.length(); i++) {
	        char ch = line.charAt(i);

	        if (ch == '"') {
	            inQuotes = !inQuotes; // toggle quote status
	        } else if (ch == ',' && !inQuotes) {
	            result.add(current.toString().trim());
	            current.setLength(0); // clear builder
	        } else {
	            current.append(ch);
	        }
	    }

	    result.add(current.toString().trim()); // add last token
	    return result;
	}

    // Writes a List of rows to the given CSV file
    public void writeCSVFile(String filePath, List<List<String>> data) throws FileException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (List<String> row : data) {
                String line = String.join(",", row);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new FileException("Error writing CSV file: " + filePath);
        }
    }
    public void saveOfficersToCSV(String filePath, List<HDBOfficer> officers) throws FileException {
        List<List<String>> data = new ArrayList<>();

        // Step 1: Add header
        data.add(Arrays.asList("Name", "NRIC", "Age", "Marital Status", "Password", "Registration Status"));

        // Step 2: Add officer data
        for (HDBOfficer officer : officers) {
            List<String> row = new ArrayList<>();
            row.add(officer.getName());
            row.add(officer.getNric());
            row.add(String.valueOf(officer.getAge()));
            row.add(officer.getMaritalStatus());
            row.add(officer.getPassword());
            row.add(officer.getRegistrationStatus()); // make sure this getter exists
            data.add(row);
        }

        // Step 3: Use your write method to save it
        writeCSVFile(filePath, data);
    }
    public void saveProjectsToCSV(String filePath, List<BTOProject> projects) throws FileException {
        List<List<String>> data = new ArrayList<>();

        // Header row
        data.add(Arrays.asList(
                "Project Name", "Neighborhood",
                "Type 1", "Number of units for Type 1", "Selling price for Type 1",
                "Type 2", "Number of units for Type 2", "Selling price for Type 2",
                "Application opening date", "Application closing date",
                "Manager", "Officer Slot", "Officer"
        ));

        for (BTOProject project : projects) {
            List<String> row = new ArrayList<>();

            row.add(project.getName());
            row.add(project.getNeighborhood());

            List<FlatType> flats = project.getFlatTypes();
            for (int i = 0; i < 2; i++) {
                if (i < flats.size()) {
                    FlatType ft = flats.get(i);
                    row.add(ft.getType());
                    row.add(String.valueOf(ft.getAvailableUnits()));
                    row.add("0"); // Placeholder for selling price (update if available)
                } else {
                    row.add("");
                    row.add("");
                    row.add("");
                }
            }

            row.add(project.getOpeningDate().toString()); // format if needed
            row.add(project.getClosingDate().toString());
            row.add(project.getManager().getName());
            row.add(String.valueOf(project.getOfficerSlots()));

            // Officers as comma-separated string in quotes
            String officers = "\"" + project.getOfficers().stream()
                    .map(User::getName)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("") + "\"";

            row.add(officers);

            data.add(row);
        }

        writeCSVFile(filePath, data);
    }

}