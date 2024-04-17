package com.example.bigsopenapiproject.domain.service;

import com.example.bigsopenapiproject.domain.entity.Town;
import com.example.bigsopenapiproject.domain.repository.TownRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class DataLoader {

    private final TownRepository townRepository;
    private final ResourceLoader resourceLoader;

    @Autowired
    public DataLoader(TownRepository townRepository, ResourceLoader resourceLoader) {
        this.townRepository = townRepository;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void loadCityData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceLoader.getResource("classpath:city_data.csv").getInputStream()))) {
            // 헤더 라인 스킵
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String regionParent = data[1];
                String regionChild = data[2];
                String regionName = data[3];
                int nx = Integer.parseInt(data[4]);
                int ny = Integer.parseInt(data[5]);

                if (!townRepository.existsByRegionName(regionName)) {
                    // 데이터가 없는 경우에만 삽입
                    Town town = new Town(regionParent, regionChild, regionName, nx, ny);
                    townRepository.save(town);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
