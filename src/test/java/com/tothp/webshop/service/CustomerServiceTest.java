package com.tothp.webshop.service;

import com.tothp.webshop.model.Customer;
import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.CustomerRepository;
import com.tothp.webshop.repository.WebshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WebshopRepository webshopRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(
                Customer.builder().id(1L).build(),
                Customer.builder().id(2L).build(),
                Customer.builder().id(3L).build()
        );
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        List<Customer> result = customerService.getAllCustomers();

        // Assert
        assertEquals(customers.size(), result.size());
        assertEquals(customers, result);
    }

    @Test
    void testGetCustomerById() {
        // Arrange
        Long id = 1L;
        Customer customer = Customer.builder().id(id).build();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        Optional<Customer> result = customerService.getCustomerById(id);

        // Assert
        assertEquals(Optional.of(customer), result);
    }

    @Test
    void testImportCustomers_ValidData() {
        // Arrange
        String filePath = "src/test/resources/test_customer.csv";

        Webshop webshop1 = Webshop.builder()
                .webshopId("WS01")
                .customers(new ArrayList<>())
                .build();

        Webshop webshop2 = Webshop.builder()
                .webshopId("WS02")
                .customers(new ArrayList<>())
                .build();

        when(webshopRepository.findById("WS01")).thenReturn(Optional.of(webshop1));
        when(webshopRepository.findById("WS02")).thenReturn(Optional.of(webshop2));
        when(customerRepository.existsByWebshopAndCustomerId(webshop1, "customer1")).thenReturn(false);
        when(customerRepository.existsByWebshopAndCustomerId(webshop2, "customer2")).thenReturn(false);

        // Act
        customerService.importCustomers(filePath);

        // Assert
        verify(customerRepository, times(2)).save(any(Customer.class));
        verify(webshopRepository, times(2)).save(any(Webshop.class));
    }


}
