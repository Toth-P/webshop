package com.tothp.webshop.service;

import com.tothp.webshop.model.Customer;
import com.tothp.webshop.model.Payment;
import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.CustomerRepository;
import com.tothp.webshop.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
}