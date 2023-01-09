package com.springboot.app.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteDto extends RouteForBookingDto{
    private Long id;
    private List<StationDto> stations;
}
