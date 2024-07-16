package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.backend.dto.MeasurementDto;
import org.example.backend.exception.CityNotFoundException;
import org.example.backend.exception.DuplicateMeasurementException;
import org.example.backend.model.City;
import org.example.backend.model.Measurement;
import org.example.backend.model.TemperatureAverage;
import org.example.backend.repository.CityRepository;
import org.example.backend.repository.MeasurementRepository;
import org.example.backend.repository.TemperatureAvarageRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneOffset;


@Service
@AllArgsConstructor
public class TemperatureService {

    private TemperatureAvarageRepository temperatureAvarageRepository;
    private MeasurementRepository measurementRepository;
    private CityRepository cityRepository;


    @Transactional
    /**
     * Check if measurement is duplicated,
     * if not add it do average
     */
    @RabbitListener(queues = {"q.new_measurements"})
    public void addNewMeasurementToDB(MeasurementDto measurementDto){
        Instant measurementInstant = measurementDto.getTime().toInstant(ZoneOffset.UTC);

        City city = cityRepository.findByName(measurementDto.getCity()).orElseGet(() -> {
            City newCity = new City();
            newCity.setName(measurementDto.getCity());
            return cityRepository.save(newCity);
        });


        if(measurementRepository.findMeasurementByCity_NameAndAndDate(measurementDto.getCity(),measurementInstant).isPresent()){
            throw new DuplicateMeasurementException(measurementDto.getCity(),measurementDto.getTime());
        }
        Measurement measurement = new Measurement();
        measurement.setCity(city);
        measurement.setDate(measurementInstant);
        measurementRepository.save(measurement);

        TemperatureAverage temperatureAverage = temperatureAvarageRepository.findTemperatureAverageByCity_Name(city.getName())
                .orElseGet(() -> {
                    TemperatureAverage newtemperatureAverage = new TemperatureAverage();
                    newtemperatureAverage.setCity(city);
                    newtemperatureAverage.setSum(new BigDecimal(0));
                    newtemperatureAverage.setNumberOfMeasurements(0l);
                    return temperatureAvarageRepository.save(newtemperatureAverage);
                });
        temperatureAverage.setSum(temperatureAverage.getSum().add(new BigDecimal(measurementDto.getValue())));
        temperatureAverage.setNumberOfMeasurements(temperatureAverage.getNumberOfMeasurements()+1);

        temperatureAvarageRepository.save(temperatureAverage);
    }

    public Double getTemperatureForCity(String city){
        TemperatureAverage temperatureAverage = temperatureAvarageRepository.findTemperatureAverageByCity_Name(city)
                .orElseThrow(() -> new CityNotFoundException(city));
        return temperatureAverage.getSum().divide(new BigDecimal(temperatureAverage.getNumberOfMeasurements()),2, RoundingMode.CEILING).doubleValue();
    }


}
