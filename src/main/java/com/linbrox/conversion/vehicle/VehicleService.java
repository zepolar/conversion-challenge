package com.linbrox.conversion.vehicle;

import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.vehicle.response.VehicleResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class VehicleService {
    private final VehicleAPI hyundaiAPI;

    public VehicleService(VehicleAPI hyundaiAPI) {
        this.hyundaiAPI = hyundaiAPI;
    }

    @CircuitBreaker(name = "externalAPI", fallbackMethod = "fallbackResponse")
    public Flux<VehicleResponse> callExternalAPI(HyundaiModelEnum hyundaiModel) {
        return this.hyundaiAPI.retrieveVehicles(hyundaiModel);
    }

    private Flux<VehicleResponse> fallbackResponse(Throwable throwable) {
        // Handle the case when the external API call fails
        return Flux.empty();
    }
}
