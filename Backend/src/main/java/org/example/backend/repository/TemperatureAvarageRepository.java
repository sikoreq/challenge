package org.example.backend.repository;

import org.example.backend.model.TemperatureAverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemperatureAvarageRepository extends JpaRepository<TemperatureAverage,Integer> {

    Optional<TemperatureAverage> findTemperatureAverageByCity_NameAndYear(String name,Integer year);

    List<TemperatureAverage> findAllByCity_Name(String name);

}
