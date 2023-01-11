package com.springboot.app.payload.station;

import lombok.Data;

import java.util.Set;

@Data
public class ListIdsStationsDto {
    private Set<Long> idsStations;
}
