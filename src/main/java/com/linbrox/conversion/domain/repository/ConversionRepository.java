package com.linbrox.conversion.domain.repository;


import com.linbrox.conversion.domain.model.Conversion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversionRepository{
    Conversion save(Conversion conversion);
    List<Conversion> findAll();
    Optional<Conversion> findById(UUID uuid);
}
