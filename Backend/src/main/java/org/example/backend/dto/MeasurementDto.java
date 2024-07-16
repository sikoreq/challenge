package org.example.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class MeasurementDto implements Serializable {
    String city;
    String time;
    Double value;
}
