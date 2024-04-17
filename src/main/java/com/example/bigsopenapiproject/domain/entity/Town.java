package com.example.bigsopenapiproject.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "town")
public class Town {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_parent")
    private String regionParent;

    @Column(name = "region_child")
    private String regionChild;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "nx")
    private int nx;

    @Column(name = "ny")
    private int ny;

    public Town(String regionParent, String regionChild, String regionName, int nx, int ny) {
        this.regionParent = regionParent;
        this.regionChild = regionChild;
        this.regionName = regionName;
        this.nx = nx;
        this.ny = ny;
    }
}
