package com.springboot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "schedule"
)
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Date departureTime;
    @Column(nullable = false)
    private Date arrivalTime;
    @Column(
            nullable = false,
            name = "class_I_available_seats"
    )
    private Integer classIAvailableSeats;
    @Column(
            nullable = false,
            name = "class_II_available_seats"
    )
    private Integer classIIAvailableSeats;
    @OneToOne
    @JoinColumn(name = "train_id")
    private Train train;

    @OneToMany(
            mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Booking> bookings = new HashSet<>();
}
