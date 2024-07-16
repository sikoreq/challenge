package org.example.backend.repository;

import org.example.backend.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,Integer> {

    //TODO change to query
    Optional<Measurement> findMeasurementByCity_NameAndAndDate(String cityName, Instant instant);
}
