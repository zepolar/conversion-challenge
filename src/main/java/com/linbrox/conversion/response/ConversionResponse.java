package com.linbrox.conversion.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConversionResponse {
    private String convertionID;
    private String conversionTimelife;
    private List<VersionResponse> versions;
}
