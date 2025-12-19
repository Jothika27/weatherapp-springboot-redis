package com.jo.weatherapp.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jo.weatherapp.dto.WeatherResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Cacheable(value = "weather", key = "#city.toLowerCase()")
    public WeatherResponse getWeather(String city) {
        log.info("Fetching weather data for city: {}", city);
        String url = apiUrl + "?key=" + apiKey + "&q=" + city + "&aqi=no";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> location = (Map<String, Object>) response.get("location");
        Map<String, Object> current = (Map<String, Object>) response.get("current");
        Map<String, Object> condition = (Map<String, Object>) current.get("condition");

        WeatherResponse weather = new WeatherResponse();
        weather.setCity(location.get("name").toString());
        weather.setTemperature((Double) current.get("temp_c"));
        weather.setHumidity((Integer) current.get("humidity"));
        weather.setWindSpeed((Double) current.get("wind_kph"));
        weather.setCondition(condition.get("text").toString());
        return weather;
    }
}
