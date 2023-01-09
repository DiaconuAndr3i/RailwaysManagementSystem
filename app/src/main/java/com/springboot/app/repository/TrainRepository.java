package com.springboot.app.repository;

import com.springboot.app.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<List<Train>> findAllByRouteId(Long id);
}
