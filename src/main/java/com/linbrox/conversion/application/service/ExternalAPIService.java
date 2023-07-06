package com.linbrox.conversion.application.service;

import com.linbrox.conversion.application.api.ConversionAPI;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.CurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.application.api.VehicleAPI;
import com.linbrox.conversion.application.api.response.VehicleResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExternalAPIService {
    private final VehicleAPI hyundaiAPI;
    private final ConversionAPI conversionAPI;

    public ExternalAPIService(VehicleAPI hyundaiAPI, ConversionAPI conversionAPI) {
        this.hyundaiAPI = hyundaiAPI;
        this.conversionAPI = conversionAPI;
    }

    @CircuitBreaker(name = "externalVehicleAPI", fallbackMethod = "fallbackVehicleResponse")
    public Flux<VehicleResponse> callVehicleAPI(HyundaiModelEnum hyundaiModel) {
        return this.hyundaiAPI.retrieveVehicles(hyundaiModel);
    }

    private Flux<VehicleResponse> fallbackVehicleResponse(Throwable throwable) {
        // Handle the case when the external API call fails
        return Flux.empty();
    }

    @CircuitBreaker(name = "externalConversionAPI", fallbackMethod = "fallbackConversionResponse")
    public Mono<Double> callConversionAPI(CryptoCurrencyEnum cryptoCurrency) {
        return this.conversionAPI.retrieveConversion(cryptoCurrency, CurrencyEnum.USD);
    }

    private Mono<Double> fallbackConversionResponse(Throwable throwable) {
        // Handle the case when the external API call fails
        return Mono.just(0d);
    }

}
