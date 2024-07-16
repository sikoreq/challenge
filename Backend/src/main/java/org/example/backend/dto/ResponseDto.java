package org.example.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ResponseDto {

    private Integer year;
    private Double averageTemperature;

}
