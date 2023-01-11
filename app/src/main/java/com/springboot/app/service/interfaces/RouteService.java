package com.springboot.app.service.interfaces;

import com.springboot.app.payload.*;
import com.springboot.app.payload.route.RouteDto;
import com.springboot.app.payload.route.RouteForBookingDto;
import com.springboot.app.payload.station.ListIdsStationsDto;
import com.springboot.app.payload.station.StationDto;

import java.util.List;


public interface RouteService {
    RouteForBookingDto createRoute(RouteDto routeDto);
    RouteForBookingDto assignStationsToRoute(Long idRoute, ListIdsStationsDto listIdsStationsDto);
    RouteForBookingDto getRouteById(Long idRoute);
    String deleteRoute(Long id);
    PagedSortedDto getAllRoutes(int pageNo, int pageSize, String sortBy, String sortDir);
    PagedSortedDto getAllRoutesWhichContainsStation(int pageNo, int pageSize, String sortBy, String sortDir, Long stationId);
    RouteForBookingDto addStationToRoute(Long idRoute, StationDto stationDto);
    RouteForBookingDto addStationToRouteById(Long idRoute, Long idStation);
    RouteForBookingDto removeStationFromRoute(Long idRoute, Long idStation);
    List<RouteForBookingDto> getRouteByDestination(String nameDestination);
}
