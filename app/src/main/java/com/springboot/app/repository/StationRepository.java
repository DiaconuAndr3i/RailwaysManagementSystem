package com.springboot.app.repository;

import com.springboot.app.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> getStationByName(String name);
}
