package com.linbrox.conversion.vehicle;

import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.vehicle.response.VehicleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    @Mock
    private VehicleAPI vehicleAPI;

    private VehicleService vehicleService;

    @BeforeEach
    void setup() {
        vehicleService = new VehicleService(vehicleAPI);
    }

    @Test
    @DisplayName("Should return empty flux when external API call fails")
    void shouldReturnEmptyFluxOnExternalAPIFailure() {
        HyundaiModelEnum hyundaiModel = HyundaiModelEnum.ACCENT;

        when(vehicleAPI.retrieveVehicles(hyundaiModel)).thenReturn(Flux.error(new Exception("External API error")));

        Flux<VehicleResponse> resultFlux = vehicleService.callExternalAPI(hyundaiModel);

        StepVerifier.create(resultFlux)
                .expectError();
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

        Flux<VehicleResponse> resultFlux = vehicleService.callExternalAPI(hyundaiModel);

        StepVerifier.create(resultFlux)
                .expectNext(vehicleResponse1, vehicleResponse2)
                .expectComplete()
                .verify();
    }
}