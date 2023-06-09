package com.tothp.webshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "webshop_id")
    private Webshop webshop;
    @Column(name = "customer_id")
    private String customerId;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private int price;
    private String accountNumber;
    private String cardNumber;
    private LocalDate dateOfPay;
}
