package com.linbrox.conversion.conversion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.CurrencyEnum;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

public class ConversionAPITest {

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
        conversionAPI = new ConversionAPI(baseUrl);
    }

    @Test
    @DisplayName("Should return a value when receive a 200 code")
    void shouldReturnWalletBodyException( ) {
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;
        CurrencyEnum currency = CurrencyEnum.USD;
        double expectedLastPrice = 123.45;
        // Create a mock response with a success status code and a JSON body
        // Create a mock response with a success status code and the JSON body
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .setBody("{ \"data\": { \"lastPrice\": " + expectedLastPrice + " } }");

        // Enqueue the mock response
        mockBackEnd.enqueue(mockResponse);

        // Execute the retriveConvertion method and verify the behavior
        Mono<Double> responseMono = conversionAPI.retrieveConvertion(cryptoCurrency, currency);

        StepVerifier.create(responseMono)
                .expectNext(expectedLastPrice)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return a mono error value when external expoint is not reached out")
    void shouldReturnZeroValue( ) {
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;
        CurrencyEnum currency = CurrencyEnum.USD;
        // Create a mock response with a success status code and a JSON body
        // Create a mock response with a success status code and the JSON body
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.BAD_REQUEST.value());

        // Enqueue the mock response
        mockBackEnd.enqueue(mockResponse);

        // Execute the retrieveVehicles method and verify the behavior
        Mono<Double> responseMono = conversionAPI.retrieveConvertion(cryptoCurrency, currency);
        // Use StepVerifier to assert the behavior
        StepVerifier.create(responseMono)
                .expectError()
                .verify();
    }

}