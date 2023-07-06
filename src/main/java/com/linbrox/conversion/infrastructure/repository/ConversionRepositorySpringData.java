package com.linbrox.conversion.infrastructure.repository;

import com.linbrox.conversion.infrastructure.entity.ConversionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversionRepositorySpringData extends JpaRepository<ConversionEntity, UUID> {
}
