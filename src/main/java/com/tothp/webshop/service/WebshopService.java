package com.tothp.webshop.service;

import com.tothp.webshop.model.Webshop;
import com.tothp.webshop.repository.WebshopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class WebshopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebshopService.class);
    @Autowired
    WebshopRepository webshopRepository;

    void createWebshops(String webshopId) {
        Webshop ws01 = Webshop.builder()
                .webshopId(webshopId)
                .customers(Collections.emptyList())
                .build();
        webshopRepository.save(ws01);
        LOGGER.info("Webshop created: webshopId={}", webshopId);
    }
}
