package com.mercan.weather.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Sensor {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;
    private String name;

}
