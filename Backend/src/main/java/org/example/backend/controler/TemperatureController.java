package org.example.backend.controler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.service.TemperatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temperature")
@AllArgsConstructor
@Slf4j
public class TemperatureController {
    private TemperatureService temperatureService;



    @GetMapping("/{city}")
    public Double getTemperature(@PathVariable String city){
        log.info("Checking temperature for City:"+city);
        return temperatureService.getTemperatureForCity(city);
    }
}
