package com.springboot.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "station")
@Builder
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;

    public boolean compareTwoStations(Station station){
        return Objects.equals(this.getId(), station.getId()) &&
                Objects.equals(this.getName(), station.getName()) &&
                Objects.equals(this.getCity(), station.getCity());
    }
}
