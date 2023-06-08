package com.tothp.webshop.repository;

import com.tothp.webshop.model.Payment;
import com.tothp.webshop.model.Webshop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByWebshop(Webshop webshop);
}
