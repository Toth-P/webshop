package com.tothp.webshop.service;

import com.tothp.webshop.exception.ImportException;
import com.tothp.webshop.model.Customer;
import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.CustomerRepository;
import com.tothp.webshop.repository.WebshopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WebshopRepository webshopRepository;

    void importCustomers(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length == 4) {
                    String webshopId = fields[0].trim();
                    String customerId = fields[1].trim();
                    String customerName = fields[2].trim();
                    String customerAddress = fields[3].trim();

                    Webshop webshop = webshopRepository.findById(webshopId)
                            .orElseThrow(() -> new ImportException("Validation error: Invalid webshop ID: webshopId=" + webshopId));

                    if (customerRepository.existsByWebshopAndCustomerId(webshop, customerId)) {
                        LOGGER.error("Validation error: Customer with the same ID already exists: webshopId={}, customerId={}", webshopId, customerId);
                        continue;
                    }

                    Customer customer = Customer.builder()
                            .customerId(customerId)
                            .customerName(customerName)
                            .customerAddress(customerAddress)
                            .webshop(webshop)
                            .build();

                    customerRepository.save(customer);

                    List<Customer> webshopCustomers = webshop.getCustomers();
                    webshopCustomers.add(customer);
                    webshopRepository.save(webshop);

                    LOGGER.info("Customer imported successfully: webshopId={}, customerId={}", webshopId, customerId);
                } else {
                    LOGGER.error("Invalid CSV format: {}", line);
                }
            }
        } catch (IOException | ImportException e) {
            LOGGER.error("Error reading customers CSV file", e);
        }
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }
}
