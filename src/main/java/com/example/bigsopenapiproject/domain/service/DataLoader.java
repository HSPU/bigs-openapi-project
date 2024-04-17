package com.example.bigsopenapiproject.domain.service;

import com.example.bigsopenapiproject.domain.entity.Town;
import com.example.bigsopenapiproject.domain.repository.TownRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
public class DataLoader {

    private final TownRepository townRepository;

    @Autowired
    public DataLoader(TownRepository townRepository) {
        this.townRepository = townRepository;
    }

    @PostConstruct
    public void loadCityData() {
        try {
            // CSV 파일 읽기
            String path = ResourceUtils.getFile("classpath:city_data.csv").getPath();
            BufferedReader reader = new BufferedReader(new FileReader(path));

            // 헤더 라인 스킵
            String headerLine = reader.readLine();

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

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
