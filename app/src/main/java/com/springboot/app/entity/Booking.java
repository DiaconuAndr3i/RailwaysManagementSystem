package com.springboot.app.entity;

import com.springboot.app.utils.Class;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "booking"
)
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            nullable = false
    )
    private Class trainClass;
    @Column(
            nullable = false
    )
    private String compartment;
    @Column(
            nullable = false
    )
    private Integer seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",
                nullable = false
    )
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id",
                nullable = false
    )
    private Schedule schedule;

}
