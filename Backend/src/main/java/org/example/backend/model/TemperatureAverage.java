package org.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.backend.model.City;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "temperature_average")
public class TemperatureAverage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "sum", nullable = false)
    private BigDecimal sum;

    @Column(name = "number_of_measurements", nullable = false)
    private Long numberOfMeasurements;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

}