package com.tothp.webshop.repository;

import com.tothp.webshop.model.Customer;
import com.tothp.webshop.model.Webshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsByWebshopAndCustomerId(Webshop webshop, String customerId);

}
