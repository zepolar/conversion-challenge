package com.linbrox.conversion.application.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.CurrencyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;


@Component
public class ConversionAPI {
    private final Logger logger = Logger.getLogger(ConversionAPI.class.getName());

    private final WebClient primaryClient;

    private final WebClient secondaryClient;

    @Autowired
    public ConversionAPI(WebClient.Builder builder, Environment env) {
        String primaryURL = env.getProperty("convertion.api");
        String secondaryURL = env.getProperty("backup.api");
        logger.info("Creating ConvertionAPI with primary "+primaryURL);
        logger.info("Creating ConvertionAPI with secondary "+primaryURL);
        this.primaryClient = builder.baseUrl(primaryURL).build();
        this.secondaryClient = builder.baseUrl(secondaryURL).build();
    }

    public ConversionAPI(String primaryURL, String secundaryURL){
        logger.info("Creating ConvertionAPI via BaseURL");
        this.primaryClient = WebClient.create(primaryURL);
        this.secondaryClient = WebClient.create(secundaryURL);
    }

    public Mono<Double> retrieveConversion(CryptoCurrencyEnum cryptoCurrency, CurrencyEnum currency){
        String uri = "/"+cryptoCurrency.name()+"/about?currency="+currency.name();
        return primaryClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        // Retrieve the lastPrice value
                        Double lastPrice = rootNode.path("data").path("lastPrice").asDouble();
                        return Mono.just(lastPrice);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .retry(3)
                .onErrorResume(throwable -> this.retrieveConversionFromSecundary(cryptoCurrency));
    }

    private Mono<Double> retrieveConversionFromSecundary(CryptoCurrencyEnum cryptoCurrency) {
        String secondaryUri = "/?id="+cryptoCurrency.getId();
        return secondaryClient.get()
                .uri(secondaryUri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        JsonNode element = rootNode.get(0);
                        // Retrieve the price_usd value
                        Double priceUsd = element.get("price_usd").asDouble();
                        return Mono.just(priceUsd);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }

}
