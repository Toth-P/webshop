package com.tothp.webshop.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ImportService {
    @Autowired
    private WebshopService webshopService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private PaymentService paymentService;

    @Transactional
    public void importData(String customerFilePath, String paymentFilePath) {
        webshopService.createWebshops("WS01");
        webshopService.createWebshops("WS02");
        customerService.importCustomers(customerFilePath);
        paymentService.importPayments(paymentFilePath);
    }

}
