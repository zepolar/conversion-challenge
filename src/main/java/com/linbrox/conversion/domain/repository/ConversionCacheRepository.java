package com.linbrox.conversion.domain.repository;

import com.linbrox.conversion.domain.model.Conversion;
import org.springframework.cache.Cache;

import java.util.UUID;

public interface ConversionCacheRepository {
    Conversion findById(UUID id) throws Exception;
    public void save(Conversion conversion);
}
