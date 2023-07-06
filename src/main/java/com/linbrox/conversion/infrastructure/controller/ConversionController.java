package com.linbrox.conversion.infrastructure.controller;

import com.linbrox.conversion.application.service.ConversionService;
import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.model.ConversionVersion;
import com.linbrox.conversion.application.api.response.ConversionResponse;
import com.linbrox.conversion.application.api.response.VersionResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversionController {
    private final ConversionService conversionService;

    public ConversionController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }


    @GetMapping("/conversion")
    @ApiOperation(value = "Conversion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something bad happened")
    })
    public Mono<ConversionResponse> conversion(
            @ApiParam(value = "Model")
            @RequestParam HyundaiModelEnum model,
            @ApiParam(value = "CryptoCurrency")
            @RequestParam CryptoCurrencyEnum cryptoCurrency
            ){
        Conversion conversion = this.conversionService.requestConversion(model, cryptoCurrency);
        List<ConversionVersion> conversionVersionList = conversion.getConversionVersionList();
        List<VersionResponse> versionResponseList = new ArrayList<>();
        for(ConversionVersion conversionVersion : conversionVersionList){
            VersionResponse versionResponse = VersionResponse.builder()
                    .version(conversionVersion.getVersion())
                    .model(conversionVersion.getHyundaiModel().name())
                    .cryptocurrency(conversionVersion.getCryptoCurrency().name())
                    .priceUsd(conversionVersion.getPriceUSD())
                    .priceCryptocurrency(conversionVersion.getPriceCryptoCurrency())
                    .cryptocurrency(conversionVersion.getCryptoCurrency().name())
                    .build();
            versionResponseList.add(versionResponse);
        }
        return Mono.just(ConversionResponse.builder()
                .convertionID(conversion.getId().toString())
                .conversionTimelife(conversion.getConvertionTimeLife())
                .versions(versionResponseList)
                .build());
    }


    @ApiOperation(value = "Retrieve all information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something bad happened")
    })
    @GetMapping("/conversion/list")
    public Flux<Conversion> findAll(){
        return Flux.fromIterable(this.conversionService.getAllConversions());
    }

    @ApiOperation(value = "Retrieve information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something bad happened")
    })
    @GetMapping("/conversion/{uuid}")
    public Mono<Conversion> retrieveById(
            @ApiParam(value = "UUID of the conversion", example = "e4eaaaf2-d142-11e1-b3e4-080027620cdd")
            @PathVariable UUID uuid){
        return conversionService.getConversionById(uuid)
                .map(Mono::just)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversion not found"));
    }
}