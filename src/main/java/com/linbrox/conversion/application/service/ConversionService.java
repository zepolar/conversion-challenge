package com.linbrox.conversion.application.service;

import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.model.ConversionVersion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversionService {

    Conversion requestConversion(HyundaiModelEnum model, CryptoCurrencyEnum cryptoCurrency);
    Conversion createConversion(String convertionTimeLife);

    Optional<Conversion> getConversionById(UUID id);

    List<Conversion> getAllConversions();

    ConversionVersion addConversionVersion(UUID conversionId, HyundaiModelEnum hyundaiModel, String version, CryptoCurrencyEnum cryptoCurrency, Double priceUSD, Double priceCryptoCurrency);
}
