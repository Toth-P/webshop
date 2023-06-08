package com.tothp.webshop;

import com.tothp.webshop.exception.ImportException;
import com.tothp.webshop.service.ImportService;
import org.springframework.stereotype.Component;

@Component
public class DataImporter {

    private final ImportService importService;

    public DataImporter(ImportService importService) {
        this.importService = importService;
    }


    public void importDataFromCSV(String customerFilePath, String paymentFilePath) throws ImportException {
        try {
            importService.importData(customerFilePath, paymentFilePath);
        } catch (Exception e) {
            throw new ImportException("Error importing data from CSV files", e);
        }
    }
}