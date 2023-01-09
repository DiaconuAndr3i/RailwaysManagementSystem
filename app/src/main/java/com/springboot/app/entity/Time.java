package com.springboot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time")
@Builder
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @Column(name = "before_id")
    private Long beforeStatId;
    @Column(name = "after_id")
    private Long afterStatId;
    private Long stationId;
}
