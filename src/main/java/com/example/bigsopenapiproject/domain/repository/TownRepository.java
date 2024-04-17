package com.example.bigsopenapiproject.domain.repository;

import com.example.bigsopenapiproject.domain.entity.Town;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TownRepository extends JpaRepository<Town, Long> {
    Optional<Town> findByNxAndNy(int nx, int ny);

    boolean existsByRegionName(String regionName);
}
