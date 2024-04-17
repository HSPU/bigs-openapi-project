package com.example.bigsopenapiproject.domain.service;

import com.example.bigsopenapiproject.domain.entity.CategoryCode;
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

    public void saveWeatherData(List<Map<String, Object>> weatherDataList) {
        for (Map<String, Object> item : weatherDataList) {
            String baseDate = (String) item.get("baseDate");
            String baseTime = (String) item.get("baseTime");
            String category = (String) item.get("category");
            String fcstDate = (String) item.get("fcstDate");
            String fcstTime = (String) item.get("fcstTime");
            String fcstValue = (String) item.get("fcstValue");
            int nx = (int) item.get("nx");
            int ny = (int) item.get("ny");

            // 카테고리 코드 해석하여 categoryName 필드에 저장
            CategoryCode code = interpretCategoryCode(category);
            fcstValue = interpretValue(category, fcstValue);

            Weather weather = new Weather();
            weather.setBaseDate(baseDate);
            weather.setBaseTime(baseTime);
            weather.setCategory(category);
            weather.setFcstDate(fcstDate);
            weather.setFcstTime(fcstTime);
            weather.setFcstValue(fcstValue);
            weather.setNx(nx);
            weather.setNy(ny);
            weather.setCategoryName(code.getName()); // 해석된 카테고리명 저장
            weather.setFcstValueUnit(code.getUnit());

            weatherRepository.save(weather);
        }
    }

    // 카테고리 코드를 해석하여 CategoryCode enum 반환하는 메서드
    private CategoryCode interpretCategoryCode(String category) {
        for (CategoryCode code : CategoryCode.values()) {
            if (code.name().equals(category)) {
                return code;
            }
        }
        return CategoryCode.ETC; // 없는 카테고리 코드인 경우
    }

    // fcstValue를 해석하여 값을 반환하는 메서드
    public String interpretValue(String category, String fcstValue) {
        // 강수형태(PTY)일 때
        if ("PTY".equals(category)) {
            switch (fcstValue) {
                case "0":
                    return "없음";
                case "1":
                    return "비";
                case "2":
                    return "비/눈";
                case "3":
                    return "눈";
                case "4":
                    return "소나기";
                default:
                    return "알 수 없음";
            }
        }
        // 하늘상태(SKY)일 때
        else if ("SKY".equals(category)) {
            switch (fcstValue) {
                case "1":
                    return "맑음";
                case "3":
                    return "구름많음";
                case "4":
                    return "흐림";
                default:
                    return "알 수 없음";
            }
        } else {
            return fcstValue;
        }
    }

    public List<Weather> getAllWeatherData() {
        return weatherRepository.findAll();
    }
}