package com.tothp.webshop.service;

import com.tothp.webshop.exception.ImportException;
import com.tothp.webshop.model.Payment;
import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.PaymentRepository;
import com.tothp.webshop.repository.WebshopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Service
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebshopRepository webshopRepository;


    void importPayments(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length == 7) {
                    String webshopId = fields[0].trim();
                    String customerId = fields[1].trim();
                    String paymentType = fields[2].trim();
                    String price = fields[3].trim();
                    String accountNumber = fields[4].trim();
                    String cardNumber = fields[5].trim();
                    String dateOfPay = fields[6].trim();

                    Webshop webshop = webshopRepository.findById(webshopId)
                            .orElseThrow(() -> new ImportException("Validation error: Invalid webshop ID: webshopId=" + webshopId));


                    if (!isValidPaymentType(paymentType)) {
                        LOGGER.error("Validation error: Invalid payment type:webshopId={}, customerId={}", webshopId, customerId);
                        continue;
                    }

                    if (!isValidDate(dateOfPay)) {
                        LOGGER.error("Validation error: Invalid date format: webshopId={}, customerId={}, dateOfPay={}", webshopId, customerId, dateOfPay);
                        continue;
                    }
                    boolean customerExists = webshop.getCustomers().stream()
                            .anyMatch(customer -> Objects.equals(customer.getCustomerId(), customerId));

                    if (!customerExists) {
                        LOGGER.error("Validation error: Customer does not exist in the webshop: webshopId={}, customerId={}", webshopId, customerId);
                        continue;
                    }


                    Payment payment = Payment.builder()
                            .webshop(webshop)
                            .customerId(customerId)
                            .paymentType(paymentType)
                            .price(Integer.parseInt(price))
                            .dateOfPay(LocalDate.parse(dateOfPay, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                            .build();

                    if (Objects.equals(payment.getPaymentType(), "card")) {
                        payment.setCardNumber(cardNumber);
                    } else {
                        payment.setAccountNumber(accountNumber);
                    }

                    paymentRepository.save(payment);
                    LOGGER.info("Payment imported successfully: webshopId={}, customerId={}", webshopId, customerId);
                } else {
                    LOGGER.error("Invalid CSV format: {}", line);
                }
            }
        } catch (IOException | ImportException e) {
            LOGGER.error("Error reading payments CSV file", e);
        }
    }


    private boolean isValidPaymentType(String paymentType) {
        return paymentType.equals("card") || paymentType.equals("transfer");
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
