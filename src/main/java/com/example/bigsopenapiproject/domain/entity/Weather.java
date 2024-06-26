package com.example.bigsopenapiproject.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id")
    private Town town;
}
