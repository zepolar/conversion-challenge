package com.linbrox.conversion.domain.repository;

import com.linbrox.conversion.domain.model.ConversionVersion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversionVersionRepository {
    void save(ConversionVersion version);
    Optional<ConversionVersion> findById(UUID id);
    List<ConversionVersion> findAll();
}
