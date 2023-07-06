package com.linbrox.conversion.infrastructure.repository;

import com.linbrox.conversion.domain.model.Conversion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ConversionRepositoryCacheTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private ConversionRepositoryCache conversionRepositoryCache;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache("conversionCache")).thenReturn(cache);
        conversionRepositoryCache = new ConversionRepositoryCache(cacheManager);
    }

    @Test
    void findById_CacheHit() throws Exception {
        UUID conversionId = UUID.randomUUID();
        Conversion expectedConversion = new Conversion();
        when(cache.get(conversionId)).thenReturn(new Cache.ValueWrapper() {
            @Override
            public Object get() {
                return expectedConversion;
            }
        });

        Conversion result = conversionRepositoryCache.findById(conversionId);

        verify(cache).get(conversionId);
        verifyNoMoreInteractions(cache);

        assertEquals(expectedConversion, result);
    }

    @Test
    void findById_CacheMiss() {
        UUID conversionId = UUID.randomUUID();
        when(cache.get(conversionId)).thenReturn(null);

        assertThrows(Exception.class, () -> conversionRepositoryCache.findById(conversionId));

        verify(cache).get(conversionId);
        verifyNoMoreInteractions(cache);
    }

    @Test
    void save() {
        UUID conversionId = UUID.randomUUID();
        Conversion conversion = new Conversion();
        conversion.setId(conversionId);

        conversionRepositoryCache.save(conversion);

        verify(cache).put(conversionId, conversion);
        verifyNoMoreInteractions(cache);
    }

}