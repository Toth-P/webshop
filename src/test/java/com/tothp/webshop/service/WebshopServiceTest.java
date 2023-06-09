package com.tothp.webshop.service;

import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.WebshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WebshopServiceTest {

    @InjectMocks
    private WebshopService webshopService;

    @Mock
    private WebshopRepository webshopRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllWebshops() {
        // Arrange
        List<Webshop> webshops = Arrays.asList(
                Webshop.builder().webshopId("WS01").build(),
                Webshop.builder().webshopId("WS02").build(),
                Webshop.builder().webshopId("WS03").build()
        );
        when(webshopRepository.findAll()).thenReturn(webshops);

        // Act
        List<Webshop> result = webshopService.getAllWebshops();

        // Assert
        assertEquals(webshops.size(), result.size());
        assertEquals(webshops, result);
    }

    @Test
    void testGetWebshopById() {
        // Arrange
        String webshopId = "WS01";
        Webshop webshop = Webshop.builder().webshopId(webshopId).build();
        when(webshopRepository.findById(webshopId)).thenReturn(Optional.of(webshop));

        // Act
        Optional<Webshop> result = webshopService.getWebshopById(webshopId);

        // Assert
        assertEquals(Optional.of(webshop), result);
    }

    @Test
    void testCreateWebshops() {
        // Arrange
        String webshopId = "WS01";
        Webshop savedWebshop = Webshop.builder()
                .webshopId(webshopId)
                .customers(Collections.emptyList())
                .build();

        when(webshopRepository.save(any(Webshop.class))).thenReturn(savedWebshop);

        // Act
        webshopService.createWebshops(webshopId);

        // Assert
        assertEquals(webshopId, savedWebshop.getWebshopId());
    }
}
