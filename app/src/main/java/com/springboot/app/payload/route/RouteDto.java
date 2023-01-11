package com.springboot.app.payload.route;

import com.springboot.app.payload.station.StationDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteDto extends RouteForBookingDto{
    private Long id;
    private List<StationDto> stations;
}
