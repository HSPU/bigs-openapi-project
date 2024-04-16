package com.example.bigsopenapiproject.domain.controller;

import com.example.bigsopenapiproject.domain.service.WeatherService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ForecastApiController {

    @Value("${serviceKey}")
    private String serviceKey;

    @Value("${apiUrl}")
    private String apiUrl;

    private final WeatherService weatherService;

    public ForecastApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/forecast")
    public ResponseEntity<String> fetchAndSaveForecast() throws IOException {
        // API를 호출하여 데이터를 가져옴
        Map<String, Object> weatherData = fetchDataFromApi();

        // 가져온 데이터를 서비스를 통해 저장
        weatherService.saveWeatherData(weatherData);

        return ResponseEntity.ok("단기예보 데이터 적재 성공");
    }

    private Map<String, Object> fetchDataFromApi() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 호출을 위한 URL 설정
        String url = apiUrl + "?serviceKey=" + serviceKey
                + "&numOfRows=20"
                + "&pageNo=1"
                + "&dataType=JSON"
                + "&base_date=20240415"
                + "&base_time=1100"
                + "&nx=62"
                + "&ny=130";

        // API 호출
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String jsonResponse = responseEntity.getBody();

        System.out.println(url);
        System.out.println("API 응답: " + jsonResponse);

        // JSON 파싱
        JSONObject jsonObject = new JSONObject(jsonResponse);
        Map<String, Object> resultMap = new HashMap<>();

        // 응답 데이터 중 필요한 부분 추출
        JSONObject response = jsonObject.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray itemList = items.getJSONArray("item");

        List<Map<String, Object>> itemListMap = new ArrayList<>();
        for (int i = 0; i < itemList.length(); i++) {
            JSONObject item = itemList.getJSONObject(i);
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("baseDate", item.getString("baseDate"));
            itemMap.put("baseTime", item.getString("baseTime"));
            itemMap.put("category", item.getString("category"));
            itemMap.put("fcstDate", item.getString("fcstDate"));
            itemMap.put("fcstTime", item.getString("fcstTime"));
            itemMap.put("fcstValue", item.getString("fcstValue"));
            itemMap.put("nx", item.getInt("nx"));
            itemMap.put("ny", item.getInt("ny"));
            itemListMap.add(itemMap);
        }

        resultMap.put("itemList", itemListMap);
        return resultMap;
    }
}