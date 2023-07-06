package com.linbrox.conversion.infrastructure.entity;


import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
import com.linbrox.conversion.domain.model.ConversionVersion;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "convertion_version")
@Data
@Builder
@NoArgsConstructor
public class ConversionVersionEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column(name = "model")
    @Enumerated(EnumType.STRING)
    private HyundaiModelEnum hyundaiModel;
    @Column(name = "version")
    private String version;
    @Column(name = "crypto_currency")
    @Enumerated(EnumType.STRING)
    private CryptoCurrencyEnum cryptoCurrency;
    @Column(name = "price_usd")
    private Double priceUSD;
    @Column(name = "price_crypto_currency")
    private Double priceCryptoCurrency;

    public ConversionVersionEntity(UUID id, HyundaiModelEnum hyundaiModel, String version, CryptoCurrencyEnum cryptoCurrency, Double priceUSD, Double priceCryptoCurrency) {
        this.id = id;
        this.hyundaiModel = hyundaiModel;
        this.version = version;
        this.cryptoCurrency = cryptoCurrency;
        this.priceUSD = priceUSD;
        this.priceCryptoCurrency = priceCryptoCurrency;
    }

    public static ConversionVersionEntity fromDomainModel(ConversionVersion conversionVersion){
        return ConversionVersionEntity.builder()
                .version(conversionVersion.getVersion())
                .id(conversionVersion.getId())
                .cryptoCurrency(conversionVersion.getCryptoCurrency())
                .hyundaiModel(conversionVersion.getHyundaiModel())
                .priceCryptoCurrency(conversionVersion.getPriceCryptoCurrency())
                .priceUSD(conversionVersion.getPriceUSD())
                .build();
    }

    public ConversionVersion toDomainModel(){
        return ConversionVersion.builder()
                .version(this.version)
                .cryptoCurrency(this.cryptoCurrency)
                .hyundaiModel(this.hyundaiModel)
                .id(this.id)
                .priceCryptoCurrency(this.priceCryptoCurrency)
                .priceUSD(this.priceUSD)
                .build();
    }
}
