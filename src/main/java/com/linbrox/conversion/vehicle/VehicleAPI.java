package com.linbrox.conversion.vehicle;

import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.vehicle.response.VehicleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.logging.Logger;

@Configuration
public class VehicleAPI {
    private final Logger logger = Logger.getLogger(VehicleAPI.class.getName());

    private final WebClient webClient;

    @Autowired
    public VehicleAPI(WebClient.Builder builder, Environment env) {
        String baseURL = env.getProperty("vehicle.api");
        logger.info("Creating VehicleAPI with base "+baseURL);
        this.webClient = builder.baseUrl(baseURL).build();
    }


    public VehicleAPI(String baseURL){
        logger.info("Creating VehicleAPI via BaseURL");
        this.webClient = WebClient.create(baseURL);
    }

    public Flux<VehicleResponse> retrieveVehicles(HyundaiModelEnum hyundaiModel){
        logger.info("Retrieving vehicles: "+hyundaiModel);
        return webClient.get()
                .uri("/list?model="+hyundaiModel.name())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(VehicleResponse.class);
    }


}
