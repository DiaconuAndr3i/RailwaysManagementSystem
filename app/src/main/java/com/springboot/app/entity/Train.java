package com.springboot.app.entity;

import com.springboot.app.utils.Type;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "train"
)
@Builder
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(
            name = "type"
    )
    private Type trainType;
    private Integer seatsFirstClass;
    private Integer seatsSecondClass;
    private Integer compartmentsNumber;
    private String estimatedTotalTime;
    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "route_id"
    )
    private Route route;
}
