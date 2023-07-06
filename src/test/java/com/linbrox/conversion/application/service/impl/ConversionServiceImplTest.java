package com.linbrox.conversion.application.service.impl;

import com.linbrox.conversion.application.api.response.VehicleResponse;
import com.linbrox.conversion.application.service.ExternalAPIService;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.model.ConversionVersion;
import com.linbrox.conversion.domain.repository.ConversionCacheRepository;
import com.linbrox.conversion.domain.repository.ConversionRepository;
import com.linbrox.conversion.domain.repository.ConversionVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConversionServiceImplTest {

    @Mock
    private ConversionRepository conversionRepository;

    @Mock
    private ConversionVersionRepository conversionVersionRepository;

    @Mock
    private ExternalAPIService externalAPIService;

    @Mock
    private ConversionCacheRepository conversionCacheRepository;

    private ConversionServiceImpl conversionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        conversionService = new ConversionServiceImpl(conversionRepository, conversionVersionRepository,
                externalAPIService, conversionCacheRepository);
    }

    @Test
    void requestConversion_Success() {
        // Mock external API responses
        HyundaiModelEnum model = HyundaiModelEnum.TUCSON;
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;
        VehicleResponse vehicleResponse1 = VehicleResponse.builder().id(UUID.randomUUID()).bonus(0L)
                .code(1234L).sgcCode("SG40").disability100(50L).name("Accent 4x4").hyundaiModel(model.name()).finalPrice(100L).salePrice(100L)
                .build();
        VehicleResponse vehicleResponse2 = VehicleResponse.builder().id(UUID.randomUUID()).bonus(0L)
                .code(1235L).sgcCode("SG40").disability100(50L).name("Accent 4x2").hyundaiModel(model.name()).finalPrice(100L).salePrice(100L)
                .build();
        List<VehicleResponse> vehicleList = Arrays.asList(vehicleResponse1, vehicleResponse2);
        Double cotizationDay = 1.0;

        when(externalAPIService.callVehicleAPI(model)).thenReturn(Flux.fromIterable(vehicleList));
        when(externalAPIService.callConversionAPI(cryptoCurrency)).thenReturn(Mono.just(cotizationDay));

        Conversion savedConversion = new Conversion();
        when(conversionRepository.save(any(Conversion.class))).thenReturn(savedConversion);

        // Perform the requestConversion operation
        Conversion result = conversionService.requestConversion(model, cryptoCurrency);

        // Verify the calls to external APIs and repositories
        verify(externalAPIService).callVehicleAPI(model);
        verify(externalAPIService).callConversionAPI(cryptoCurrency);
        verify(conversionRepository).save(any(Conversion.class));
        verify(conversionCacheRepository).save(any(Conversion.class));

        // Verify the returned Conversion object
        assertNotNull(result);

    }

    @Test
    void requestConversion_NoVehiclesFound() {
        // Mock external API response with an empty list
        HyundaiModelEnum model = HyundaiModelEnum.ACCENT;
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;

        when(externalAPIService.callVehicleAPI(model)).thenReturn(Flux.empty());

        // Perform the requestConversion operation and expect an exception
        assertThrows(RuntimeException.class, () -> conversionService.requestConversion(model, cryptoCurrency));

        // Verify the calls to external APIs and repositories
        verify(externalAPIService).callVehicleAPI(model);
        verifyNoInteractions(conversionRepository, conversionCacheRepository);
    }

    @Test
    void requestConversion_ZeroConversionRate() {
        // Mock external API response with a zero conversion rate
        HyundaiModelEnum model = HyundaiModelEnum.TUCSON;
        CryptoCurrencyEnum cryptoCurrency = CryptoCurrencyEnum.BTC;
        VehicleResponse vehicleResponse1 = VehicleResponse.builder().id(UUID.randomUUID()).bonus(0L)
                .code(1234L).sgcCode("SG40").disability100(50L).name("Accent 4x4").hyundaiModel(model.name()).finalPrice(100L).salePrice(100L)
                .build();
        VehicleResponse vehicleResponse2 = VehicleResponse.builder().id(UUID.randomUUID()).bonus(0L)
                .code(1235L).sgcCode("SG40").disability100(50L).name("Accent 4x2").hyundaiModel(model.name()).finalPrice(100L).salePrice(100L)
                .build();
        List<VehicleResponse> vehicleList = Arrays.asList(vehicleResponse1, vehicleResponse2);
        Double cotizationDay = 0.0;

        when(externalAPIService.callVehicleAPI(model)).thenReturn(Flux.fromIterable(vehicleList));
        when(externalAPIService.callConversionAPI(cryptoCurrency)).thenReturn(Mono.just(cotizationDay));

        // Perform the requestConversion operation and expect an exception
        assertThrows(RuntimeException.class, () -> conversionService.requestConversion(model, cryptoCurrency));

        // Verify the calls to external APIs and repositories
        verify(externalAPIService).callVehicleAPI(model);
        verify(externalAPIService).callConversionAPI(cryptoCurrency);
        verifyNoInteractions(conversionRepository, conversionCacheRepository);
    }
}