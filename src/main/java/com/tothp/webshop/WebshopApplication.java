package com.tothp.webshop;

import com.tothp.webshop.exception.ImportException;
import com.tothp.webshop.service.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebshopApplication {

    private static final String CUSTOMER_FILE_PATH = "csv/customer.csv";
    private static final String PAYMENT_FILE_PATH = "csv/payments.csv";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebshopApplication.class, args);

        DataImporter csvDataImporter = context.getBean(DataImporter.class);
        try {
            csvDataImporter.importDataFromCSV(CUSTOMER_FILE_PATH, PAYMENT_FILE_PATH);
        } catch (ImportException e) {
            System.err.println("Error importing data from CSV files: " + e.getMessage());
        }
    }

}
