package com.linbrox.conversion.domain.model;


import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
public class ConversionVersion {
    private UUID id;
    private HyundaiModelEnum hyundaiModel;
    private String version;
    private CryptoCurrencyEnum cryptoCurrency;
    private Double priceUSD;
    private Double priceCryptoCurrency;
    private Conversion conversion;

    public ConversionVersion(UUID id, HyundaiModelEnum hyundaiModel, String version, CryptoCurrencyEnum cryptoCurrency, Double priceUSD, Double priceCryptoCurrency, Conversion conversion) {
        this.id = id;
        this.hyundaiModel = hyundaiModel;
        this.version = version;
        this.cryptoCurrency = cryptoCurrency;
        this.priceUSD = priceUSD;
        this.priceCryptoCurrency = priceCryptoCurrency;
        this.conversion = conversion;
    }
}
