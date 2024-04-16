package com.example.bigsopenapiproject.domain.repository;

import com.example.bigsopenapiproject.domain.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
