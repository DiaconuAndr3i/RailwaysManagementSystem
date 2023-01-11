package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Station;
import com.springboot.app.entity.Train;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.*;
import com.springboot.app.payload.route.RouteDto;
import com.springboot.app.payload.route.RouteForBookingDto;
import com.springboot.app.payload.station.ListIdsStationsDto;
import com.springboot.app.payload.station.StationDto;
import com.springboot.app.repository.RouteRepository;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.RouteService;
import com.springboot.app.service.interfaces.StationService;
import com.springboot.app.utils.pagination.SortPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final StationService stationService;
    private final RouteRepository routeRepository;
    private final ObjectsMapperService objectsMapperService;
    private final TrainRepository trainRepository;

    @Override
    public RouteForBookingDto createRoute(RouteDto routeDto) {
        return saveRouteAndMapToDto((Route) objectsMapperService
                .mapFromTo(routeDto, new Route()));
    }

    @Override
    public RouteForBookingDto assignStationsToRoute(Long idRoute, ListIdsStationsDto listIdsStationsDto) {
        listIdsStationsDto.getIdsStations().forEach(id -> addStationToRouteById(idRoute, id));
        return getRouteById(idRoute);
    }

    @Override
    public RouteForBookingDto getRouteById(Long idRoute) {
        return (RouteForBookingDto) objectsMapperService
                .mapFromTo(getRouteByIdAsRoute(idRoute), new RouteDto());
    }

    @Override
    public String deleteRoute(Long id) {
        Route route = getRouteByIdAsRoute(id);
        List<Train> trainList = trainRepository.findAllByRouteId(id).orElseThrow(() -> new ResourceNotFoundException("Train", "id", id));
        trainList.forEach(train -> train.setRoute(null));
        trainRepository.saveAll(trainList);
        route.setStations(null);
        routeRepository.delete(route);
        return "Route was deleted successfully!";
    }

    @Override
    public PagedSortedDto getAllRoutes(int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Route> routePages = routeRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<RouteForBookingDto> routes = routePages.getContent()
                .stream()
                .map(route -> (RouteForBookingDto) objectsMapperService.mapFromTo(route, new RouteDto()))
                .toList();
        return objectsMapperService.mapToSortedPaged(routes, routePages);
    }

    @Override
    public PagedSortedDto getAllRoutesWhichContainsStation(int pageNo, int pageSize, String sortBy, String sortDir, Long stationId) {
        Page<Route> routePages = routeRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<RouteForBookingDto> routes = routePages.getContent()
                .stream()
                .filter(route -> route.stationsContains(stationId))
                .map(route -> (RouteForBookingDto) objectsMapperService.mapFromTo(route, new RouteDto()))
                .toList();
        if(routes.size()==0){
            throw new ResourceNotFoundException("Station", "id", stationId);
        }
        return objectsMapperService.mapToSortedPaged(routes, routePages);
    }

    @Override
    public RouteForBookingDto addStationToRoute(Long idRoute, StationDto stationDto) {
        Route route = getRouteByIdAsRoute(idRoute);
        route.addStationToRoute((Station) objectsMapperService
                .mapFromTo(stationService.insertStation(stationDto), new Station()));
        return saveRouteAndMapToDto(route);
    }

    @Override
    public RouteForBookingDto addStationToRouteById(Long idRoute, Long idStation) {
        Route route = getRouteByIdAsRoute(idRoute);
        StationDto stationDto = stationService.getStationById(idStation);
        route.addStationToRoute((Station) objectsMapperService.mapFromTo(stationDto, new Station()));
        return saveRouteAndMapToDto(route);
    }

    @Override
    public RouteForBookingDto removeStationFromRoute(Long idRoute, Long idStation) {
        Route route = getRouteByIdAsRoute(idRoute);
        StationDto stationDto = stationService.getStationById(idStation);
        route.removeStationFromRoute((Station) objectsMapperService.mapFromTo(stationDto, new Station()));
        return saveRouteAndMapToDto(route);
    }

    @Override
    public List<RouteForBookingDto> getRouteByDestination(String nameDestination) {
        List<Route> routes = routeRepository.findAllByToPlace(nameDestination).orElseThrow(() -> new ResourceNotFoundException("Route", "toPlace", nameDestination));
        if(routes.size()==0){
            throw new ResourceNotFoundException("Route", "toPlace", nameDestination);
        }
        return routes.stream()
                .map(route -> (RouteDto)objectsMapperService.mapFromTo(route, new RouteDto()))
                .collect(Collectors.toList());
    }

    private RouteForBookingDto saveRouteAndMapToDto(Route route){
        return (RouteForBookingDto) objectsMapperService
                .mapFromTo(routeRepository.save(route), new RouteDto());
    }

    private Route getRouteByIdAsRoute(Long id){
        return routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));
    }
}
