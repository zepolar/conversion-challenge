package com.linbrox.conversion.entity;


import com.linbrox.conversion.common.CryptoCurrencyEnum;
import com.linbrox.conversion.common.HyundaiModelEnum;
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
public class ConversionVersion {
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

    public ConversionVersion(UUID id, HyundaiModelEnum hyundaiModel, String version, CryptoCurrencyEnum cryptoCurrency, Double priceUSD, Double priceCryptoCurrency) {
        this.id = id;
        this.hyundaiModel = hyundaiModel;
        this.version = version;
        this.cryptoCurrency = cryptoCurrency;
        this.priceUSD = priceUSD;
        this.priceCryptoCurrency = priceCryptoCurrency;
    }
}
