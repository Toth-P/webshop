package com.tothp.webshop;

import com.tothp.webshop.exception.ImportException;
import com.tothp.webshop.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebshopApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebshopApplication.class);

    private static final String CUSTOMER_FILE_PATH = "src/main/resources/inputFiles/customer.csv";
    private static final String PAYMENT_FILE_PATH = "src/main/resources/inputFiles/payments.csv";

    private static final String REPORT01_FILE_PATH = "src/main/resources/reports/report01.csv";
    private static final String REPORT02_FILE_PATH = "src/main/resources/reports/report02.csv";
    private static final String TOP_REPORT_FILE_PATH = "src/main/resources/reports/top.csv";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebshopApplication.class, args);

        DataImporter csvDataImporter = context.getBean(DataImporter.class);
        try {
            csvDataImporter.importDataFromCSV(CUSTOMER_FILE_PATH, PAYMENT_FILE_PATH);
            ReportService reportService = context.getBean(ReportService.class);
            reportService.generateCustomerTotalPurchasesReport(REPORT01_FILE_PATH);
            reportService.generateTopCustomersReport(REPORT01_FILE_PATH, TOP_REPORT_FILE_PATH);
            reportService.generateWebshopReports(REPORT02_FILE_PATH);
        } catch (ImportException e) {
            LOGGER.error("Error importing data from CSV files: " + e.getMessage());
        }
    }

}
