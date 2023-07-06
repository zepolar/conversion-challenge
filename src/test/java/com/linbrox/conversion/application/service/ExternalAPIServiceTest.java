package com.linbrox.conversion.application.service;

import com.linbrox.conversion.application.api.ConversionAPI;
import com.linbrox.conversion.application.api.VehicleAPI;
import com.linbrox.conversion.application.api.response.VehicleResponse;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.CurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class ExternalAPIServiceTest {
    @Mock
    private VehicleAPI vehicleAPI;

    @Mock
    private ConversionAPI conversionAPI;

    private ExternalAPIService externalAPIService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        externalAPIService = new ExternalAPIService(vehicleAPI, conversionAPI);
    }

    @Test
    @DisplayName("Should return empty flux when external API call fails")
    void shouldReturnEmptyFluxOnExternalAPIFailure() {
        HyundaiModelEnum hyundaiModel = HyundaiModelEnum.ACCENT;

        when(vehicleAPI.retrieveVehicles(hyundaiModel)).thenReturn(Flux.error(new Exception("External API error")));

        Flux<VehicleResponse> resultFlux = externalAPIService.callVehicleAPI(hyundaiModel);

        StepVerifier.create(resultFlux)
                .expectError()
                .verify();
    }

    @Test
    @DisplayName("Should return vehicle response flux from external API")
    void shouldReturnVehicleResponseFluxFromExternalAPI() {
        HyundaiModelEnum hyundaiModel = HyundaiModelEnum.ACCENT;
        VehicleResponse vehicleResponse1 = VehicleResponse.builder().id(UUID.randomUUID()).bonus(0L)
                .code(1234L).sgcCode("SG40").disability100(50L).name("Accent 4x4").hyundaiModel(hyundaiModel.name()).finalPrice(100L).salePrice(100L)
                .build();
        VehicleResponse vehicleResponse2 = VehicleResponse.builder().id(UUID.randomUUID()).bonus(0L)
                .code(1235L).sgcCode("SG40").disability100(50L).name("Accent 4x2").hyundaiModel(hyundaiModel.name()).finalPrice(100L).salePrice(100L)
                .build();
        List<VehicleResponse> expectedResponseList = Arrays.asList(vehicleResponse1, vehicleResponse2);

        when(vehicleAPI.retrieveVehicles(hyundaiModel))
                .thenReturn(Flux.fromIterable(expectedResponseList));

        Flux<VehicleResponse> resultFlux = externalAPIService.callVehicleAPI(hyundaiModel);

        StepVerifier.create(resultFlux)
                .expectNext(vehicleResponse1, vehicleResponse2)
                .expectComplete()
                .verify();
    }

    @Test
    void callConversionAPI_Success() {
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;
        double conversionResult = 1000.0;
        when(conversionAPI.retrieveConversion(cryptoCurrency, CurrencyEnum.USD)).thenReturn(Mono.just(conversionResult));

        Mono<Double> result = externalAPIService.callConversionAPI(cryptoCurrency);

        verify(conversionAPI).retrieveConversion(cryptoCurrency, CurrencyEnum.USD);
        verifyNoMoreInteractions(conversionAPI);
        result
                .as(StepVerifier::create)
                .expectNext(conversionResult)
                .verifyComplete();
    }

    @Test
    void callConversionAPI_Fallback() {
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;
        when(conversionAPI.retrieveConversion(cryptoCurrency, CurrencyEnum.USD))
                .thenReturn(Mono.error(new Exception("External API error")));
        Mono<Double> result = externalAPIService.callConversionAPI(cryptoCurrency);

        verify(conversionAPI).retrieveConversion(cryptoCurrency, CurrencyEnum.USD);
        verifyNoMoreInteractions(conversionAPI);

        StepVerifier.create(result)
                .expectError()
                .verify();
    }
}