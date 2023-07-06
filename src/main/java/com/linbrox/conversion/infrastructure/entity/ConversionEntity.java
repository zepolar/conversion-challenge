package com.linbrox.conversion.infrastructure.entity;

import com.linbrox.conversion.domain.model.Conversion;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "convertion")
@Data
@Builder
@NoArgsConstructor
public class ConversionEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "convertion_time_life")
    private String convertionTimeLife;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConversionVersionEntity> conversionVersionEntityList;


    public ConversionEntity(UUID id, String convertionTimeLife, Date createdAt, List<ConversionVersionEntity> conversionVersionEntityList) {
        this.id = id;
        this.convertionTimeLife = convertionTimeLife;
        this.createdAt = createdAt;
        this.conversionVersionEntityList = conversionVersionEntityList;
    }

    public static ConversionEntity fromDomainModel(Conversion conversion){
        return ConversionEntity.builder()
                .id(conversion.getId())
                .convertionTimeLife(conversion.getConvertionTimeLife())
                .createdAt(conversion.getCreatedAt())
                .conversionVersionEntityList(conversion.getConversionVersionList().stream().map(ConversionVersionEntity::fromDomainModel).collect(Collectors.toList()))
                .build();
    }

    public Conversion toDomainModel(){
        return Conversion.builder()
                .convertionTimeLife(this.convertionTimeLife)
                .createdAt(this.createdAt)
                .conversionVersionList(this.conversionVersionEntityList.stream().map(ConversionVersionEntity::toDomainModel).collect(Collectors.toList()))
                .id(this.id)
                .build();
    }
}
