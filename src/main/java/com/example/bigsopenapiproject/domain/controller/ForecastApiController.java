package com.example.bigsopenapiproject.domain.controller;

import com.example.bigsopenapiproject.domain.dto.WeatherDto;
import com.example.bigsopenapiproject.domain.entity.Weather;
import com.example.bigsopenapiproject.domain.service.JsonParserService;
import com.example.bigsopenapiproject.domain.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public ResponseEntity<String> fetchAndSaveForecast() {
        try {
            String jsonResponse = fetchDataFromApi();
            log.info("받은 JSON 응답: {}", jsonResponse);

            // JSON 데이터 파싱
            List<Map<String, Object>> weatherData = jsonParserService.parseJsonResponse(jsonResponse);
            weatherService.saveWeatherData(weatherData);

            return ResponseEntity.ok("단기예보 데이터 적재 성공");
        } catch (IOException e) {
            log.error("API 호출 또는 데이터 파싱 중 예외 발생 : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("단기예보 데이터 적재 실패: " + e.getMessage());
        }
    }

    private String fetchDataFromApi() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = buildApiUrl();

        log.info("API URL: {}", url);

        // API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return responseEntity.getBody();
    }

    // API URL 설정 (경기도 의정부시 고산동 (송산1동) : NX = 62, NY = 130)
    private String buildApiUrl() {
        return apiUrl + "?serviceKey=" + serviceKey
                + "&numOfRows=20"
                + "&pageNo=1"
                + "&dataType=JSON"
                + "&base_date=20240416"
                + "&base_time=1100"
                + "&nx=62"
                + "&ny=130";
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherDto>> getAllWeatherData() {
        List<Weather> weatherData = weatherService.getAllWeatherData();
        List<WeatherDto> weatherDTOList = weatherData.stream()
                .map(WeatherDto::new)
                .collect(Collectors.toList());
        if (weatherDTOList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(weatherDTOList);
    }
}