package com.springboot.app.payload;

import lombok.Data;

@Data
public class RouteForBookingDto {
    private String fromPlace;
    private String toPlace;
}
