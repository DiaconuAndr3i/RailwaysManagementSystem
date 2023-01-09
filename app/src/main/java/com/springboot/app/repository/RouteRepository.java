package com.springboot.app.repository;

import com.springboot.app.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<List<Route>> findByStationsId(Long id);
}
