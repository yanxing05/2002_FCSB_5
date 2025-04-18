package bto.system.services;

import bto.system.exceptions.FileException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFileService {

    // Reads CSV file and returns data as List of rows (List<String>)
    public List<List<String>> readCSVFile(String filePath) throws FileException {
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                List<String> rowData = Arrays.asList(line.split(","));
                data.add(rowData);
            }
        } catch (IOException e) {
            throw new FileException("Error reading CSV file: " + filePath);
        }

        return data;
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
}
