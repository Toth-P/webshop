package com.tothp.webshop.service;

import com.tothp.webshop.model.Customer;
import com.tothp.webshop.model.Payment;
import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.PaymentRepository;
import com.tothp.webshop.repository.WebshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private WebshopRepository webshopRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPayments() {
        // Arrange
        List<Payment> payments = Arrays.asList(
                Payment.builder().id(1L).build(),
                Payment.builder().id(2L).build(),
                Payment.builder().id(3L).build()
        );
        when(paymentRepository.findAll()).thenReturn(payments);

        // Act
        List<Payment> result = paymentService.getAllPayments();

        // Assert
        assertEquals(payments.size(), result.size());
        assertEquals(payments, result);
    }

    @Test
    void testGetPaymentById() {
        // Arrange
        Long id = 1L;
        Payment payment = Payment.builder().id(id).build();
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        // Act
        Optional<Payment> result = paymentService.getPaymentById(id);

        // Assert
        assertEquals(Optional.of(payment), result);
    }

    @Test
    void testImportPayments_ValidData() {
        // Arrange
        String filePath = "src/test/resources/test_payments.csv";

        Webshop webshop1 = Webshop.builder()
                .webshopId("WS01")
                .customers(Collections.singletonList(Customer.builder()
                        .customerId("A01")
                        .build()))
                .build();

        Webshop webshop2 = Webshop.builder()
                .webshopId("WS02")
                .customers(Collections.singletonList(Customer.builder()
                        .customerId("A02")
                        .build()))
                .build();

        when(webshopRepository.findById("WS01")).thenReturn(Optional.of(webshop1));
        when(webshopRepository.findById("WS02")).thenReturn(Optional.of(webshop2));

        // Act
        paymentService.importPayments(filePath);

        // Assert
        verify(paymentRepository, times(2)).save(any(Payment.class));
    }
}
