package com.linbrox.conversion.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "convertion")
@Data
@Builder
@NoArgsConstructor
public class Conversion {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "convertion_time_life")
    private String convertionTimeLife;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConversionVersion> conversionVersionList;

    public Conversion(UUID id, String convertionTimeLife, Date createdAt, List<ConversionVersion> conversionVersionList) {
        this.id = id;
        this.convertionTimeLife = convertionTimeLife;
        this.createdAt = createdAt;
        this.conversionVersionList = conversionVersionList;
    }
}
