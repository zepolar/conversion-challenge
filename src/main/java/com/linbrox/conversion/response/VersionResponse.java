package com.linbrox.conversion.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VersionResponse {
    private String model;
    private String version;
    private Double priceUsd;
    private Double priceCryptocurrency;
    private String cryptocurrency;
}
