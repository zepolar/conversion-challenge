package com.linbrox.conversion.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.CurrencyEnum;
import com.linbrox.conversion.conversion.ConversionAPI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class ConversionServiceTest {
    public static MockWebServer mockBackEnd;
    private ObjectMapper MAPPER = new ObjectMapper();
    private ConversionAPI conversionAPI;

    @BeforeAll
    static void setUp( ) throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown( ) throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void initialize( ) {
        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
        conversionAPI = new ConversionAPI((baseUrl));
    }

    @Test
    void testRetrieveVehicles_CircuitBreakerOpen() {
        // Set up a mock response with an error status code
        MockResponse mockResponse = new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        mockBackEnd.enqueue(mockResponse);
        // Execute the retrieveVehicles method and verify the behavior
        Mono<Double> responseFlux = conversionAPI.retrieveConvertion(CryptoCurrencyEnum.BTC, CurrencyEnum.USD);
        StepVerifier.create(responseFlux)
                .expectError()
                .verify();
    }
}