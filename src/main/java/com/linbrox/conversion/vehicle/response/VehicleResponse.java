package com.linbrox.conversion.vehicle.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class VehicleResponse {
    private UUID id;
    private Long code;
    private String name;
    private Long year;
    private Long salePrice;
    private Long bonus;
    private Long finalPrice;
    private Long disability100;
    private String sgcCode;
    private String hyundaiModel;
}