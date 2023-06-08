package com.tothp.webshop.service;

import com.tothp.webshop.model.Customer;
import com.tothp.webshop.model.CustomerPurchase;
import com.tothp.webshop.model.Payment;
import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.CustomerRepository;
import com.tothp.webshop.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public ReportService(CustomerRepository customerRepository, PaymentRepository paymentRepository) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
    }

    public void generateCustomerTotalPurchasesReport(String reportFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(reportFilePath))) {
            List<Customer> customers = customerRepository.findAll();
            for (Customer customer : customers) {
                String customerName = customer.getCustomerName();
                String customerAddress = customer.getCustomerAddress();
                int purchaseTotal = getPurchaseTotalByCustomer(customer);

                String line = String.format("%s;%s;%d", customerName, customerAddress, purchaseTotal);
                writer.println(line);
            }

            LOGGER.info("Customer report generated successfully.");
        } catch (IOException e) {
            LOGGER.error("Error generating customer report: {}", e.getMessage());
        }
    }

    private int getPurchaseTotalByCustomer(Customer customer) {
        Webshop webshop = customer.getWebshop();
        List<Payment> payments = paymentRepository.findByWebshop(webshop);
        int purchaseTotal = 0;
        for (Payment payment : payments) {
            if (payment.getCustomerId().equals(customer.getCustomerId())) {
                purchaseTotal += payment.getPrice();
            }
        }
        return purchaseTotal;
    }
    public void generateTopCustomersReport(String inputFilePath, String outputFilePath) {
        List<CustomerPurchase> customerPurchases = readReportFile(inputFilePath);
        customerPurchases.sort(Comparator.comparingInt(CustomerPurchase::getTotalPurchase).reversed());
        List<CustomerPurchase> topTwoCustomers = customerPurchases.subList(0, Math.min(customerPurchases.size(), 2));

        saveTopCustomersReport(topTwoCustomers, outputFilePath);
    }

    private List<CustomerPurchase> readReportFile(String filePath) {
        List<CustomerPurchase> customerPurchases = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length == 3) {
                    String name = fields[0].trim();
                    String address = fields[1].trim();
                    int totalPurchase = Integer.parseInt(fields[2].trim());

                    CustomerPurchase customerPurchase = new CustomerPurchase(name, address, totalPurchase);
                    customerPurchases.add(customerPurchase);
                } else {
                    System.err.println("Invalid CSV format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading report file: " + e.getMessage());
        }

        return customerPurchases;
    }

    private void saveTopCustomersReport(List<CustomerPurchase> topTwoCustomers, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (CustomerPurchase customer : topTwoCustomers) {
                writer.write(customer.getName() + ";" + customer.getAddress() + ";" + customer.getTotalPurchase());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }
}