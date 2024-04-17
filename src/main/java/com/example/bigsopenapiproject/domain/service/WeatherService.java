package com.example.bigsopenapiproject.domain.service;

import com.example.bigsopenapiproject.domain.entity.CategoryCode;
import com.example.bigsopenapiproject.domain.entity.Town;
import com.example.bigsopenapiproject.domain.entity.Weather;
import com.example.bigsopenapiproject.domain.repository.TownRepository;
import com.example.bigsopenapiproject.domain.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final TownRepository townRepository;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, TownRepository townRepository) {
        this.weatherRepository = weatherRepository;
        this.townRepository = townRepository;
    }

    public void saveWeatherData(List<Map<String, Object>> weatherDataList) {
        for (Map<String, Object> item : weatherDataList) {
            saveWeatherItem(item);
        }
    }

    private void saveWeatherItem(Map<String, Object> item) {
        String baseDate = (String) item.get("baseDate");
        String baseTime = (String) item.get("baseTime");
        String category = (String) item.get("category");
        String fcstDate = (String) item.get("fcstDate");
        String fcstTime = (String) item.get("fcstTime");
        int nx = (int) item.get("nx");
        int ny = (int) item.get("ny");

        CategoryCode code = interpretCategoryCode(category);
        String fcstValue = interpretValue(category, (String) item.get("fcstValue"));

        Town town = findTownByNxNy(nx, ny);

        Weather weather = createWeather(baseDate, baseTime, category, fcstDate, fcstTime, nx, ny, code, fcstValue, town);
        weatherRepository.save(weather);
    }

    private Town findTownByNxNy(int nx, int ny) {
        return townRepository.findByNxAndNy(nx, ny)
                .orElseThrow(() -> new RuntimeException("도시 정보를 찾을 수 없습니다. : nx = " + nx + ", ny = " + ny));
    }

    private Weather createWeather(String baseDate, String baseTime, String category, String fcstDate,
                                  String fcstTime, int nx, int ny, CategoryCode code, String fcstValue, Town town) {
        Weather weather = new Weather();
        weather.setBaseDate(baseDate);
        weather.setBaseTime(baseTime);
        weather.setCategory(category);
        weather.setFcstDate(fcstDate);
        weather.setFcstTime(fcstTime);
        weather.setFcstValue(fcstValue);
        weather.setNx(nx);
        weather.setNy(ny);
        weather.setCategoryName(code.getName());
        weather.setFcstValueUnit(code.getUnit());
        weather.setTown(town);
        return weather;
    }

    // 카테고리 코드를 해석하여 CategoryCode enum 반환
    private CategoryCode interpretCategoryCode(String category) {
        for (CategoryCode code : CategoryCode.values()) {
            if (code.name().equals(category)) {
                return code;
            }
        }
        return CategoryCode.ETC;
    }

    // fcstValue를 해석하여 값을 반환
    public String interpretValue(String category, String fcstValue) {
        // 강수형태(PTY)일 때
        if ("PTY".equals(category)) {
            return switch (fcstValue) {
                case "0" -> "없음";
                case "1" -> "비";
                case "2" -> "비/눈";
                case "3" -> "눈";
                case "4" -> "소나기";
                default -> "알 수 없음";
            };
        }
        // 하늘상태(SKY)일 때
        else if ("SKY".equals(category)) {
            return switch (fcstValue) {
                case "1" -> "맑음";
                case "3" -> "구름많음";
                case "4" -> "흐림";
                default -> "알 수 없음";
            };
        } else {
            return fcstValue;
        }
    }

    public List<Weather> getAllWeatherData() {
        return weatherRepository.findAll();
    }
}