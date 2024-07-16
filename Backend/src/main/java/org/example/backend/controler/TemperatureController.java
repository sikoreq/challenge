package org.example.backend.controler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.dto.MeasurementDto;
import org.example.backend.dto.ResponseDto;
import org.example.backend.service.TemperatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/temperature")
@AllArgsConstructor
@Slf4j
public class TemperatureController {
    private TemperatureService temperatureService;


    @PostMapping("/loadFromFile")
    public ResponseEntity<?> loadFromFile() throws IOException, URISyntaxException {
        temperatureService.loadFromFile();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addNewTemperature(@RequestBody MeasurementDto dto){
        temperatureService.addMessageToQueue(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{city}")
    public List<ResponseDto> getTemperature(@PathVariable String city){
        log.info("Checking temperature for City: "+city);
        return temperatureService.getTemperatureForCity(city);
    }
}
