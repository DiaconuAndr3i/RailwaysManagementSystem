package com.springboot.app.payload;

import lombok.Data;

import java.util.Set;

@Data
public class ListIdsStationsDto {
    private Set<Long> idsStations;
}
