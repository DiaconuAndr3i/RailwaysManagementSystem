package com.springboot.app.service.interfaces;

import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.StationDto;

public interface StationService {
    StationDto insertStation(StationDto stationDto);
    StationDto getStationById(Long id);
    PagedSortedDto getAllStations(int pageNo, int pageSize, String sortBy, String sortDir);
    StationDto getStationByName(String name);
    StationDto updateStation(StationDto stationDto, Long idStation);
    String deleteStation(Long id);
}
