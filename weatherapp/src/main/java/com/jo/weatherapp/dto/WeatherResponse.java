package com.jo.weatherapp.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class WeatherResponse implements Serializable {
    private String city;
    private double temperature;
    private String condition;
    private int humidity;
    private double windSpeed;
}
