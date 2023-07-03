package com.linbrox.conversion.service;


import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.CurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.conversion.ConversionAPI;
import com.linbrox.conversion.entity.Conversion;
import com.linbrox.conversion.entity.ConversionVersion;
import com.linbrox.conversion.repository.ConversionRepository;
import com.linbrox.conversion.vehicle.VehicleService;
import com.linbrox.conversion.vehicle.response.VehicleResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class ConversionService {
    private final ConversionAPI conversionAPI;
    private final ConversionRepository conversionRepository;
    private final VehicleService vehicleService;

    public ConversionService(ConversionAPI conversionAPI, ConversionRepository conversionRepository, VehicleService vehicleService) {
        this.conversionAPI = conversionAPI;
        this.conversionRepository = conversionRepository;
        this.vehicleService = vehicleService;
    }

    @CircuitBreaker(name = "externalAPI", fallbackMethod = "fallbackResponse")
    public Mono<Double> callExternalAPI(CryptoCurrencyEnum cryptoCurrency) {
        return this.conversionAPI.retrieveConvertion(cryptoCurrency, CurrencyEnum.USD);
    }

    private Mono<Double> fallbackResponse(Throwable throwable) {
        // Handle the case when the external API call fails
        return Mono.just(0d);
    }

    public Conversion requestConvertion(HyundaiModelEnum hyundaiModel, CryptoCurrencyEnum cryptoCurrency){
        List<VehicleResponse> vechileList = this.vehicleService.callExternalAPI(hyundaiModel).collectList().block();
        if(vechileList.isEmpty()){
            //something is wrong
            throw new RuntimeException("Something is wrong");
        }
        Double cotizationDay = this.callExternalAPI(cryptoCurrency).block();
        if(cotizationDay==0d){
            //something is wrong
            throw new RuntimeException("Something is wrong");
        }
        else{
            List<ConversionVersion> convertionVersionList = new ArrayList<>();
            for(VehicleResponse vehicle: vechileList){
                ConversionVersion convertionVersion = ConversionVersion.builder()
                        .hyundaiModel(hyundaiModel)
                        .version(vehicle.getName())
                        .priceUSD(vehicle.getSalePrice().doubleValue())
                        .cryptoCurrency(cryptoCurrency)
                        .priceCryptoCurrency(vehicle.getFinalPrice()/cotizationDay)
                        .build();
                convertionVersionList.add(convertionVersion);
            }
            Conversion conversion = Conversion.builder()
                    .createdAt(new Date())
                    .conversionVersionList(convertionVersionList)
                    .convertionTimeLife("Algo")
                    .build();
            return this.conversionRepository.save(conversion);
        }
    }

    @Cacheable(value = "convertionCache", key = "#uuid")
    public Conversion retrieveById(String uuid){
        UUID id = UUID.fromString(uuid);
        Optional<Conversion> convertionOptional = this.conversionRepository.findById(id);
        if(convertionOptional.isEmpty()){
            //throw new ConvertionException("Entity not found");
            throw new RuntimeException("Entity not found");
        }
        else{
            return convertionOptional.get();
        }
    }

    public List<Conversion> findAll(){
        return this.conversionRepository.findAll();
    }

}
