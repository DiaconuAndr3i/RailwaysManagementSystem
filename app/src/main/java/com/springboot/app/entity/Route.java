package com.springboot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "route"
)
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fromPlace;
    private String toPlace;
    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Train> trains = new HashSet<>();
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "route_station",
            joinColumns = @JoinColumn(
                    name = "route_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "station_id",
                    referencedColumnName = "id"
            )
    )
    private Set<Station> stations = new HashSet<>();

    public void addStationToRoute(Station station){
        stations.add(station);
    }
    public void removeStationFromRoute(Station station){
        stations.removeIf(localStation -> localStation.compareTwoStations(station));
    }
}
