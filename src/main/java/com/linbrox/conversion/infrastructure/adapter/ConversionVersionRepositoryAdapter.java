package com.linbrox.conversion.infrastructure.adapter;

import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.model.ConversionVersion;
import com.linbrox.conversion.domain.repository.ConversionRepository;
import com.linbrox.conversion.domain.repository.ConversionVersionRepository;
import com.linbrox.conversion.infrastructure.entity.ConversionVersionEntity;
import com.linbrox.conversion.infrastructure.repository.ConversionVersionRepositorySpringData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class ConversionVersionRepositoryAdapter implements ConversionVersionRepository {

    private final ConversionVersionRepositorySpringData conversionVersionRepositorySpringData;

    public ConversionVersionRepositoryAdapter(ConversionVersionRepositorySpringData conversionVersionRepositorySpringData) {
        this.conversionVersionRepositorySpringData = conversionVersionRepositorySpringData;
    }


    @Override
    public void save(ConversionVersion version) {
        ConversionVersionEntity entity = ConversionVersionEntity.fromDomainModel(version);
        this.conversionVersionRepositorySpringData.save(entity);
    }

    @Override
    public Optional<ConversionVersion> findById(UUID id) {
        return this.conversionVersionRepositorySpringData.findById(id)
                .map(ConversionVersionEntity::toDomainModel);
    }


    @Override
    public List<ConversionVersion> findAll() {
        return this.conversionVersionRepositorySpringData.findAll()
                .stream().map(ConversionVersionEntity::toDomainModel)
                .collect(Collectors.toList());
    }
}
