package org.example.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "city")
    private Set<TemperatureAverage> temperatureAverages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "city")
    private Set<Measurement> measurements = new LinkedHashSet<>();

}