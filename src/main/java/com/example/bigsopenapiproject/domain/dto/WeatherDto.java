package com.example.bigsopenapiproject.domain.dto;

import com.example.bigsopenapiproject.domain.entity.Weather;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDto {
    private Long id;
    private String baseDate;
    private String baseTime;
    private String categoryName;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private String fcstValueUnit;
    private int nx;
    private int ny;
    private Long townId;

    public WeatherDto(Weather weather) {
        this.id = weather.getId();
        this.baseDate = weather.getBaseDate();
        this.baseTime = weather.getBaseTime();
        this.categoryName = weather.getCategoryName();
        this.category = weather.getCategory();
        this.fcstDate = weather.getFcstDate();
        this.fcstTime = weather.getFcstTime();
        this.fcstValue = weather.getFcstValue();
        this.fcstValueUnit = weather.getFcstValueUnit();
        this.nx = weather.getNx();
        this.ny = weather.getNy();

        if (weather.getTown() != null) {
            townId = weather.getTown().getId();
        } else {
            townId = null;
        }
    }
}

