package com.linbrox.conversion.infrastructure.adapter;

import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.repository.ConversionCacheRepository;
import com.linbrox.conversion.infrastructure.repository.ConversionRepositoryCache;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ConversionCacheRepositoryAdapter implements ConversionCacheRepository {
    private final ConversionRepositoryCache conversionRepositoryCache;

    public ConversionCacheRepositoryAdapter(ConversionRepositoryCache conversionRepositoryCache) {
        this.conversionRepositoryCache = conversionRepositoryCache;
    }

    public Conversion findById(UUID id) throws Exception {
        return conversionRepositoryCache.findById(id);
    }

    public void save(Conversion conversion) {
        conversionRepositoryCache.save(conversion);
    }
}
