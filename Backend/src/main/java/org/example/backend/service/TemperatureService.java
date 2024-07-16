package org.example.backend.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.MeasurementDto;
import org.example.backend.dto.ResponseDto;
import org.example.backend.exception.CityNotFoundException;
import org.example.backend.exception.DuplicateMeasurementException;
import org.example.backend.model.City;
import org.example.backend.model.Measurement;
import org.example.backend.model.TemperatureAverage;
import org.example.backend.repository.CityRepository;
import org.example.backend.repository.MeasurementRepository;
import org.example.backend.repository.TemperatureAvarageRepository;
import org.example.backend.utils.DateFormater;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@AllArgsConstructor
@Slf4j
public class TemperatureService {

    private TemperatureAvarageRepository temperatureAvarageRepository;
    private MeasurementRepository measurementRepository;
    private CityRepository cityRepository;
    private Queue newMeasurementsQueue;
    private Queue errorQueue;
    private RabbitTemplate rabbitTemplate;


    public void loadFromFile() throws IOException, URISyntaxException {
        Path file = Path.of(ClassLoader.getSystemResource("example_file.csv").toURI());
        Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8);
        lines.forEach(row -> {
            String[] values = row.split(";");
            MeasurementDto dto = MeasurementDto.builder()
                    .city(values[0])
                    .time(values[1])
                    .value(Double.valueOf(values[2]))
                    .build();
            addMessageToQueue(dto);

        });


    }

    /**
     * Persist measurements in Rabbit
     */
    public void addMessageToQueue(MeasurementDto measurementDto){
        rabbitTemplate.convertAndSend(newMeasurementsQueue.getName(),measurementDto);
        log.info("Message successfully added to Queue");
    }


    @Transactional
    /**
     * Check if measurement is duplicated,
     * if not add it do average
     */
    @RabbitListener(queues = {"q.new_measurements"})
    public void addNewMeasurementToDB(MeasurementDto measurementDto){
        log.info("get message from queue");
        try {
            LocalDateTime localDateTime = DateFormater.convertFromString(measurementDto.getTime());
            Instant measurementInstant = localDateTime.toInstant(ZoneOffset.UTC);

            City city = cityRepository.findByName(measurementDto.getCity()).orElseGet(() -> {
                City newCity = new City();
                newCity.setName(measurementDto.getCity());
                return cityRepository.save(newCity);
            });


            if (measurementRepository.findMeasurementByCity_NameAndAndDate(measurementDto.getCity(), measurementInstant).isPresent()) {
                throw new DuplicateMeasurementException(measurementDto.getCity(), localDateTime);
            }
            Measurement measurement = new Measurement();
            measurement.setCity(city);
            measurement.setDate(measurementInstant);
            measurementRepository.save(measurement);

            TemperatureAverage temperatureAverage = temperatureAvarageRepository.findTemperatureAverageByCity_NameAndYear(city.getName(), localDateTime.getYear())
                    .orElseGet(() -> {
                        TemperatureAverage newtemperatureAverage = new TemperatureAverage();
                        newtemperatureAverage.setCity(city);
                        newtemperatureAverage.setSum(new BigDecimal(0));
                        newtemperatureAverage.setNumberOfMeasurements(0l);
                        newtemperatureAverage.setYear(localDateTime.getYear());
                        return temperatureAvarageRepository.save(newtemperatureAverage);
                    });
            temperatureAverage.setSum(temperatureAverage.getSum().add(new BigDecimal(measurementDto.getValue())));
            temperatureAverage.setNumberOfMeasurements(temperatureAverage.getNumberOfMeasurements() + 1);

            temperatureAvarageRepository.save(temperatureAverage);
        }catch (Exception e){
            log.error(e.getMessage());
            rabbitTemplate.convertAndSend(errorQueue.getName(),e.getMessage());
        }
    }

    /**
     * Get arerage temperature for city
     * @param city city name
     * @return average temperature
     */
    public List<ResponseDto> getTemperatureForCity(String city){
        List<TemperatureAverage> temperatureAverage = temperatureAvarageRepository.findAllByCity_Name(city);
        return temperatureAverage.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

    }

    public ResponseDto convertToResponseDto(TemperatureAverage temperatureAverage){
        return ResponseDto.builder()
                .year(temperatureAverage.getYear())
                .averageTemperature(temperatureAverage.getSum().divide(new BigDecimal(temperatureAverage.getNumberOfMeasurements()),2, RoundingMode.CEILING).doubleValue())
                .build();

    }


}
