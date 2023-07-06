package com.linbrox.conversion.infrastructure.adapter;

import com.linbrox.conversion.domain.model.Conversion;
import com.linbrox.conversion.domain.repository.ConversionRepository;
import com.linbrox.conversion.infrastructure.entity.ConversionEntity;
import com.linbrox.conversion.infrastructure.repository.ConversionRepositorySpringData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ConversionRepositoryAdapter implements ConversionRepository {


    private final ConversionRepositorySpringData conversionRepositorySpringData;

    public ConversionRepositoryAdapter(ConversionRepositorySpringData conversionRepositorySpringData) {
        this.conversionRepositorySpringData = conversionRepositorySpringData;
    }

    @Override
    public Conversion save(Conversion conversion) {
        ConversionEntity conversionEntity = ConversionEntity.fromDomainModel(conversion);
        return this.conversionRepositorySpringData.save(conversionEntity).toDomainModel();
    }

    @Override
    public List<Conversion> findAll() {
        return this.conversionRepositorySpringData.findAll()
                .stream().map(ConversionEntity::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Conversion> findById(UUID uuid) {
        return this.conversionRepositorySpringData
                .findById(uuid)
                .map(ConversionEntity::toDomainModel);
    }
}
