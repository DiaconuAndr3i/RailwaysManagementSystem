package com.springboot.app.payload.route;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RouteForBookingDto {
    @NotEmpty(message = "fromPlace cannot be null or empty")
    private String fromPlace;
    @NotEmpty(message = "toPlace cannot be null or empty")
    private String toPlace;
}
