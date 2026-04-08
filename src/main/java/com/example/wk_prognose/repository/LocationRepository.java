package com.example.wk_prognose.repository;

import com.example.wk_prognose.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByOrderByCountryAscCityAscStadiumNameAsc();
}
