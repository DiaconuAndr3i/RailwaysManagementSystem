package com.springboot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "customer",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})}
)
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            nullable = false
    )
    private String firstName;
    @Column(
            nullable = false
    )
    private String lastName;
    @Column(
            nullable = false
    )
    private String email;

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Booking> bookings = new HashSet<>();
}
