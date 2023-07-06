package com.linbrox.conversion.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
public class Conversion {
    private UUID id;
    private String convertionTimeLife;
    private Date createdAt;
    private List<ConversionVersion> conversionVersionList;

    public Conversion(UUID id, String convertionTimeLife, Date createdAt, List<ConversionVersion> conversionVersionList) {
        this.id = id;
        this.convertionTimeLife = convertionTimeLife;
        this.createdAt = createdAt;
        this.conversionVersionList = conversionVersionList;
    }
}
