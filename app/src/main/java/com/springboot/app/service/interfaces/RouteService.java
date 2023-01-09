package com.springboot.app.service.interfaces;

import com.springboot.app.payload.*;


public interface RouteService {
    RouteForBookingDto createRoute(RouteDto routeDto);
    RouteForBookingDto assignStationsToRoute(Long idRoute, ListIdsStationsDto listIdsStationsDto);
    RouteForBookingDto getRouteById(Long idRoute);
    String deleteRoute(Long id);
    PagedSortedDto getAllRoutes(int pageNo, int pageSize, String sortBy, String sortDir);
    RouteForBookingDto addStationToRoute(Long idRoute, StationDto stationDto);
    RouteForBookingDto addStationToRouteById(Long idRoute, Long idStation);
    RouteForBookingDto removeStationFromRoute(Long idRoute, Long idStation);
}
