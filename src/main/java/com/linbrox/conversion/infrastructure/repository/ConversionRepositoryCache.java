package com.linbrox.conversion.infrastructure.repository;

import com.linbrox.conversion.domain.model.Conversion;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ConversionRepositoryCache {
    private final Cache cache;

    public ConversionRepositoryCache(CacheManager cacheManager){
        this.cache = cacheManager.getCache("conversionCache");
    }

    public Conversion findById(UUID id) throws Exception {
        Cache.ValueWrapper valueWrapper = cache.get(id);
        if(valueWrapper!=null){
            return (Conversion) valueWrapper.get();
        }
        throw new Exception("");
    }

    public void save(Conversion conversion){
        cache.put(conversion.getId(), conversion);
    }

}
