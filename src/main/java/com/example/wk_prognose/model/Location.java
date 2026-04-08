package com.example.wk_prognose.model;

import com.example.wk_prognose.util.HostCountry;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String stadiumName;

    @Enumerated(EnumType.STRING)
    private HostCountry country;

    public Location(String city, String stadiumName, HostCountry country){
        this.city = city;
        this.stadiumName = stadiumName;
        this.country = country;
    }
}
