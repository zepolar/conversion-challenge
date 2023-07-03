package com.linbrox.conversion.repository;


import com.linbrox.conversion.entity.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, UUID> {

}
