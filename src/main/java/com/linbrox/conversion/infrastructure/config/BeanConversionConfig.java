package com.linbrox.conversion.infrastructure.config;

import com.linbrox.conversion.application.service.ConversionService;
import com.linbrox.conversion.application.service.ExternalAPIService;
import com.linbrox.conversion.application.service.impl.ConversionServiceImpl;
import com.linbrox.conversion.domain.repository.ConversionCacheRepository;
import com.linbrox.conversion.domain.repository.ConversionRepository;
import com.linbrox.conversion.domain.repository.ConversionVersionRepository;
import com.linbrox.conversion.infrastructure.adapter.ConversionCacheRepositoryAdapter;
import com.linbrox.conversion.infrastructure.adapter.ConversionRepositoryAdapter;
import com.linbrox.conversion.infrastructure.adapter.ConversionVersionRepositoryAdapter;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConversionConfig {

    @Bean
    ConversionService conversionBeanService(
            final ConversionRepository conversionRepository,
            final ConversionVersionRepository conversionVersionRepository,
            final ExternalAPIService externalAPIService,
            final ConversionCacheRepository conversionCacheRepository
            ){
        return new ConversionServiceImpl(conversionRepository, conversionVersionRepository, externalAPIService,  conversionCacheRepository);
    }

    @Bean ConversionRepository conversionRepository(ConversionRepositoryAdapter conversionRepositoryAdapter){
        return conversionRepositoryAdapter;
    }

    @Bean ConversionVersionRepository conversionVersionRepository(ConversionVersionRepositoryAdapter conversionVersionRepository){
        return conversionVersionRepository;
    }

    @Bean ConversionCacheRepository conversionCacheRepository(ConversionCacheRepositoryAdapter conversionCacheRepositoryAdapter){
        return conversionCacheRepositoryAdapter;
    }
}
