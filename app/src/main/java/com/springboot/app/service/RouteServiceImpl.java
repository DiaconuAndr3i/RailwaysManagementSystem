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
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Override
    public RouteForBookingDto createRoute(RouteDto routeDto) {
        return saveRouteAndMapToDto(modelMapper.map(routeDto, Route.class));
    }

    @Override
    public RouteForBookingDto assignStationsToRoute(Long idRoute, ListIdsStationsDto listIdsStationsDto) {
        listIdsStationsDto.getIdsStations().forEach(id -> addStationToRouteById(idRoute, id));
        return getRouteById(idRoute);
    }

    @Override
    public RouteForBookingDto getRouteById(Long idRoute) {
        return modelMapper.map(getRouteByIdAsRoute(idRoute), RouteDto.class);
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
                .map(route -> (RouteForBookingDto) modelMapper.map(route, RouteDto.class))
                .toList();
        return objectsMapperService.mapToSortedPaged(routes, routePages);
    }

    @Override
    public PagedSortedDto getAllRoutesWhichContainsStation(int pageNo, int pageSize, String sortBy, String sortDir, Long stationId) {
        Page<Route> routePages = routeRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<RouteForBookingDto> routes = routePages.getContent()
                .stream()
                .filter(route -> route.stationsContains(stationId))
                .map(route -> (RouteForBookingDto) modelMapper.map(route, RouteDto.class))
                .toList();
        if(routes.size()==0){
            throw new ResourceNotFoundException("Station", "id", stationId);
        }
        return objectsMapperService.mapToSortedPaged(routes, routePages);
    }

    @Override
    public RouteForBookingDto addStationToRoute(Long idRoute, StationDto stationDto) {
        Route route = getRouteByIdAsRoute(idRoute);
        route.addStationToRoute(modelMapper.map(stationService.insertStation(stationDto), Station.class));
        return saveRouteAndMapToDto(route);
    }

    @Override
    public RouteForBookingDto addStationToRouteById(Long idRoute, Long idStation) {
        Route route = getRouteByIdAsRoute(idRoute);
        StationDto stationDto = stationService.getStationById(idStation);
        route.addStationToRoute(modelMapper.map(stationDto, Station.class));
        return saveRouteAndMapToDto(route);
    }

    @Override
    public RouteForBookingDto removeStationFromRoute(Long idRoute, Long idStation) {
        Route route = getRouteByIdAsRoute(idRoute);
        StationDto stationDto = stationService.getStationById(idStation);
        route.removeStationFromRoute(modelMapper.map(stationDto, Station.class));
        return saveRouteAndMapToDto(route);
    }

    @Override
    public List<RouteForBookingDto> getRouteByDestination(String nameDestination) {
        List<Route> routes = routeRepository.findAllByToPlace(nameDestination).orElseThrow(() -> new ResourceNotFoundException("Route", "toPlace", nameDestination));
        if(routes.size()==0){
            throw new ResourceNotFoundException("Route", "toPlace", nameDestination);
        }
        return routes.stream()
                .map(route -> modelMapper.map(route, RouteDto.class))
                .collect(Collectors.toList());
    }

    public RouteForBookingDto saveRouteAndMapToDto(Route route){
        return modelMapper.map(routeRepository.save(route), RouteDto.class);
    }

    public Route getRouteByIdAsRoute(Long id){
        return routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));
    }
}
