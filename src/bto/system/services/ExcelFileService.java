package bto.system.services;

import bto.system.exceptions.FileException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelFileService {

    public List<List<String>> readExcelFile(String filePath) throws FileException {
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                // Split the line by comma and trim each value
                List<String> rowData = Arrays.asList(line.split(","));
                data.add(rowData);
            }
        } catch (IOException e) {
            throw new FileException("Error reading CSV file: " + filePath);
        }

        return data;
    }
}
