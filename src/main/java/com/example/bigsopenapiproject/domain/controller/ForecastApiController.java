package com.example.bigsopenapiproject.domain.controller;

import com.example.bigsopenapiproject.domain.service.JsonParserService;
import com.example.bigsopenapiproject.domain.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ForecastApiController {

    @Value("${serviceKey}")
    private String serviceKey;

    @Value("${apiUrl}")
    private String apiUrl;

    private final WeatherService weatherService;
    private final JsonParserService jsonParserService;

    public ForecastApiController(WeatherService weatherService, JsonParserService jsonParserService) {
        this.weatherService = weatherService;
        this.jsonParserService = jsonParserService;
    }

    @PostMapping("/forecast")
    public ResponseEntity<String> fetchAndSaveForecast() throws IOException {
        // API를 호출하여 데이터를 가져옴
        String jsonResponse = fetchDataFromApi();

        // JSON log
        log.info(jsonResponse);

        // JSON 데이터 파싱
        List<Map<String, Object>> weatherData = jsonParserService.parseJsonResponse(jsonResponse);

        weatherService.saveWeatherData(weatherData);

        return ResponseEntity.ok("단기예보 데이터 적재 성공");
    }

    private String fetchDataFromApi() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출을 위한 URL 설정
        String url = apiUrl + "?serviceKey=" + serviceKey
                + "&numOfRows=20"
                + "&pageNo=1"
                + "&dataType=JSON"
                + "&base_date=20240416"
                + "&base_time=1100"
                + "&nx=62"
                + "&ny=130";

        // URL log
        log.info(url);

        // API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return responseEntity.getBody();
    }
}