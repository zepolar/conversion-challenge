package com.linbrox.conversion.application.service.impl;


import com.linbrox.conversion.application.api.response.VehicleResponse;
import com.linbrox.conversion.application.service.ConversionService;
import com.linbrox.conversion.application.service.ExternalAPIService;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.model.ConversionVersion;
import com.linbrox.conversion.domain.repository.ConversionCacheRepository;
import com.linbrox.conversion.domain.repository.ConversionRepository;
import com.linbrox.conversion.domain.repository.ConversionVersionRepository;

import java.util.*;


public class ConversionServiceImpl implements ConversionService {
    private final ConversionRepository conversionRepository;

    private final ConversionCacheRepository conversionCacheRepository;
    private final ConversionVersionRepository conversionVersionRepository;
    private final ExternalAPIService externalAPIService;

    public ConversionServiceImpl(ConversionRepository conversionRepository,
                                 ConversionVersionRepository conversionVersionRepository,
                                 ExternalAPIService externalAPIService,
                                 ConversionCacheRepository conversionCacheRepository) {
        this.conversionRepository = conversionRepository;
        this.conversionVersionRepository = conversionVersionRepository;
        this.externalAPIService = externalAPIService;
        this.conversionCacheRepository = conversionCacheRepository;
    }


    @Override
    public Conversion requestConversion(HyundaiModelEnum model, CryptoCurrencyEnum cryptoCurrency) {
        List<VehicleResponse> vechileList = this.externalAPIService.callVehicleAPI(model).collectList().block();
        if(vechileList.isEmpty()){
            //something is wrong
            throw new RuntimeException("Something is wrong");
        }
        Double cotizationDay = this.externalAPIService.callConversionAPI(cryptoCurrency).block();
        if(cotizationDay==0d){
            //something is wrong
            throw new RuntimeException("Something is wrong");
        }
        else{
            List<ConversionVersion> convertionVersionList = new ArrayList<>();
            for(VehicleResponse vehicle: vechileList){
                ConversionVersion convertionVersion = ConversionVersion.builder()
                        .id(UUID.randomUUID())
                        .hyundaiModel(model)
                        .version(vehicle.getName())
                        .priceUSD(vehicle.getSalePrice().doubleValue())
                        .cryptoCurrency(cryptoCurrency)
                        .priceCryptoCurrency(vehicle.getFinalPrice()/cotizationDay)
                        .build();
                convertionVersionList.add(convertionVersion);
            }
            Conversion conversion = Conversion.builder()
                    .createdAt(new Date())
                    .id(UUID.randomUUID())
                    .convertionTimeLife("20TTL")
                    .conversionVersionList(convertionVersionList)
                    .build();
            this.conversionRepository.save(conversion);
            this.conversionCacheRepository.save(conversion);
            return conversion;
        }
    }

    @Override
    public Conversion createConversion(String convertionTimeLife) {
        Conversion conversion = Conversion.builder()
                .id(UUID.randomUUID())
                .convertionTimeLife(convertionTimeLife)
                .createdAt(new Date())
                .build();
        conversionRepository.save(conversion);
        return conversion;
    }

    @Override
    public Optional<Conversion> getConversionById(UUID id) {
        try{
            return Optional.of(this.conversionCacheRepository.findById(id));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public List<Conversion> getAllConversions() {
        return conversionRepository.findAll();
    }

    @Override
    public ConversionVersion addConversionVersion(UUID conversionId, HyundaiModelEnum hyundaiModel,
                                                  String versionVehiclo, CryptoCurrencyEnum cryptoCurrency, Double priceUSD, Double priceCryptoCurrency) {
        Optional<Conversion> optionalConversion = conversionRepository.findById(conversionId);
        if (optionalConversion.isPresent()) {
            Conversion conversion = optionalConversion.get();
            ConversionVersion conversionVersion = ConversionVersion.builder()
                    .conversion(conversion)
                    .id(UUID.randomUUID())
                    .hyundaiModel(hyundaiModel)
                    .version(versionVehiclo)
                    .cryptoCurrency(cryptoCurrency)
                    .priceUSD(priceUSD)
                    .priceCryptoCurrency(priceCryptoCurrency)
                    .build();
            conversionVersionRepository.save(conversionVersion);
            return conversionVersion;
        }
        throw new IllegalArgumentException("Conversion not found.");
    }

}
