package com.springboot.app.payload.route;

import lombok.Data;

@Data
public class RouteForBookingDto {
    private String fromPlace;
    private String toPlace;
}
