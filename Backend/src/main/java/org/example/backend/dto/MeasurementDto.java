package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MeasurementDto {
    String City;
    LocalDateTime time;
    Double value;
}
