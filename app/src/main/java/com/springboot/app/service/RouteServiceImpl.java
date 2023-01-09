package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Station;
import com.springboot.app.entity.Train;
import com.springboot.app.payload.*;
import com.springboot.app.repository.RouteRepository;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.RouteService;
import com.springboot.app.service.interfaces.StationService;
import com.springboot.app.utils.SortPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Train> trainList = trainRepository.findAllByRouteId(id).orElseThrow(RuntimeException::new);
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

    private RouteForBookingDto saveRouteAndMapToDto(Route route){
        return (RouteForBookingDto) objectsMapperService
                .mapFromTo(routeRepository.save(route), new RouteDto());
    }

    private Route getRouteByIdAsRoute(Long id){
        return routeRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
