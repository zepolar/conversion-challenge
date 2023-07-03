package com.linbrox.conversion.conversion;

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

    private final WebClient webClient;

    @Autowired
    public ConversionAPI(WebClient.Builder builder, Environment env) {
        String baseURL = env.getProperty("convertion.api");
        logger.info("Creating ConvertionAPI with base "+baseURL);
        this.webClient = builder.baseUrl(baseURL).build();
    }


    public ConversionAPI(String baseURL){
        logger.info("Creating ConvertionAPI via BaseURL");
        this.webClient = WebClient.create(baseURL);
    }

    public Mono<Double> retrieveConvertion(CryptoCurrencyEnum cryptoCurrency, CurrencyEnum currency){
        String uri = "/"+cryptoCurrency.name()+"/about?currency="+currency.name();
        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(responseBody);
                        // Retrieve the lastPrice value
                        double lastPrice = rootNode.path("data").path("lastPrice").asDouble();
                        return Mono.just(lastPrice);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
}
