package com.springboot.app.payload.station;

import lombok.Data;

@Data
public class StationDto {
    private Long id;
    private String name;
    private String city;
}