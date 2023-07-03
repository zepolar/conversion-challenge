package com.linbrox.conversion.controller;

import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.entity.Conversion;
import com.linbrox.conversion.entity.ConversionVersion;
import com.linbrox.conversion.response.ConversionResponse;
import com.linbrox.conversion.response.VersionResponse;
import com.linbrox.conversion.service.ConversionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ConversionResponse converation(
            @ApiParam(value = "Model")
            @RequestParam HyundaiModelEnum model,
            @ApiParam(value = "CryptoCurrency")
            @RequestParam CryptoCurrencyEnum cryptoCurrency
            ){
        Conversion conversion = this.conversionService.requestConvertion(model, cryptoCurrency);
        List<VersionResponse> versionResponseList = new ArrayList<>();
        for(ConversionVersion conversionVersion: conversion.getConversionVersionList()){
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
        return ConversionResponse.builder()
                .convertionID(conversion.getId().toString())
                .conversionTimelife(conversion.getConvertionTimeLife())
                .versions(versionResponseList)
                .build();
    }


    @ApiOperation(value = "Retrieve all information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something bad happened")
    })
    @GetMapping("/conversion/list")
    public List<Conversion> findAll(){
        return this.conversionService.findAll();
    }



    @ApiOperation(value = "Retrieve information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Something bad happened")
    })
    @GetMapping("/conversion/{uuid}")
    public Conversion retrieveById(
            @ApiParam(value = "UUID of the conversion", example = "e4eaaaf2-d142-11e1-b3e4-080027620cdd")
            @PathVariable UUID uuid){
            return this.conversionService.retrieveById(uuid.toString());
    }
}