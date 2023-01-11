package com.springboot.app.payload.station;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StationDto {
    private Long id;
    @NotEmpty(message = "Name cannot be empty or null")
    private String name;
    @NotEmpty(message = "City cannot be empty or null")
    private String city;
}
