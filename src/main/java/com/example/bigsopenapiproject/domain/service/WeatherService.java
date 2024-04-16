package com.example.bigsopenapiproject.domain.service;

import com.example.bigsopenapiproject.domain.entity.Weather;
import com.example.bigsopenapiproject.domain.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public void saveWeatherData(Map<String, Object> weatherDataMap) {
        // JSON 데이터 파싱 및 매핑하여 Weather 생성 및 저장
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) weatherDataMap.get("itemList");

        for (Map<String, Object> item : itemList) {
            String baseDate = (String) item.get("baseDate");
            String baseTime = (String) item.get("baseTime");
            String category = (String) item.get("category");
            String fcstDate = (String) item.get("fcstDate");
            String fcstTime = (String) item.get("fcstTime");
            String fcstValue = (String) item.get("fcstValue");
            int nx = (int) item.get("nx");
            int ny = (int) item.get("ny");

            Weather weather = new Weather();
            weather.setBaseDate(baseDate);
            weather.setBaseTime(baseTime);
            weather.setCategory(category);
            weather.setFcstDate(fcstDate);
            weather.setFcstTime(fcstTime);
            weather.setFcstValue(fcstValue);
            weather.setNx(nx);
            weather.setNy(ny);

            weatherRepository.save(weather);
        }
    }
}