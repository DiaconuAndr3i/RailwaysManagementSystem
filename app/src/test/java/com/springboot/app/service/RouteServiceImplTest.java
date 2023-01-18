package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Station;
import com.springboot.app.entity.Train;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.route.RouteDto;
import com.springboot.app.payload.route.RouteForBookingDto;
import com.springboot.app.payload.station.ListIdsStationsDto;
import com.springboot.app.payload.station.StationDto;
import com.springboot.app.repository.RouteRepository;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.StationService;
import com.springboot.app.utils.pagination.SortPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {
    @InjectMocks
    private RouteServiceImpl routeService;
    @Mock
    private StationService stationService;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private ObjectsMapperService objectsMapperService;
    @Mock
    private TrainRepository trainRepository;
    @Mock
    private ModelMapper modelMapper;
    private final Route route;
    private final RouteDto routeDto;
    private final StationDto stationDto;
    private final Station station;
    private final Train train;

    public RouteServiceImplTest(){
        station = new Station();
        station.setId(1L);
        station.setName("Station");
        station.setCity("City");

        train = Train.builder()
                .compartmentsNumber(4)
                .estimatedTotalTime("1:45")
                .seatsFirstClass(1)
                .seatsSecondClass(2)
                .build();

        route = Route.builder()
                .id(1L)
                .fromPlace("CityRoute1")
                .toPlace("CityRoute2")
                .stations(new HashSet<>())
                .trains(new HashSet<>())
                .build();

        routeDto = new RouteDto();
        routeDto.setFromPlace("CityRoute1");
        routeDto.setToPlace("CityRoute2");

        stationDto = new StationDto();
        stationDto.setName("Station");
        stationDto.setCity("City");
    }

    @Test
    void createRoute() {
        when(modelMapper.map(routeDto, Route.class)).thenReturn(route);
        when(routeRepository.save(route)).thenReturn(route);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        RouteForBookingDto routeDtoResult = routeService.createRoute(routeDto);
        verify(routeRepository, times(1)).save(route);
        assertEquals(routeDto.getFromPlace(), routeDtoResult.getFromPlace());
        assertEquals(routeDto.getToPlace(), routeDtoResult.getToPlace());
    }

    @Test
    void assignStationsToRoute() {
        ListIdsStationsDto listIdsStationsDto = new ListIdsStationsDto();
        listIdsStationsDto.setIdsStations(Set.of(1L));

        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(stationService.getStationById(1L)).thenReturn(stationDto);
        when(modelMapper.map(stationDto, Station.class)).thenReturn(station);
        when(routeRepository.save(route)).thenReturn(route);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);

        routeService.assignStationsToRoute(1L, listIdsStationsDto);

        verify(routeRepository, times(2)).findById(1L);
        verify(stationService, times(1)).getStationById(1L);
        verify(modelMapper, times(2)).map(route, RouteDto.class);
        verify(routeRepository, times(1)).save(route);
        verify(modelMapper, times(1)).map(stationDto, Station.class);
    }

    @Test
    void getRouteById() {
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        routeService.getRouteById(1L);
        verify(modelMapper, times(1)).map(route, RouteDto.class);
        verify(routeRepository, times(1)).findById(1L);
    }

    @Test
    void deleteRoute() {
        String expected = "Route was deleted successfully!";
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        List<Train> trainList = List.of(train);
        when(trainRepository.findAllByRouteId(1L)).thenReturn(Optional.of(trainList));
        String result = routeService.deleteRoute(1L);
        verify(trainRepository, times(1)).saveAll(trainList);
        verify(routeRepository, times(1)).delete(route);
        assertEquals(expected, result);
    }

    @Test
    void getAllRoutes() {
        int pageNo = 5, pageSize = 1;
        String sortBy = "id", sortDir = "asc";
        Page<Route> routePages = new PageImpl<>(new ArrayList<>(List.of(route)));
        when(routeRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir))).thenReturn(routePages);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        List<RouteForBookingDto> routes = List.of(routeDto);
        PagedSortedDto pagedSortedDto = PagedSortedDto.builder()
                .pageNo(routePages.getNumber())
                .totalElements(routePages.getTotalElements())
                .pageSize(routePages.getSize())
                .totalPages(routePages.getTotalPages())
                .first(routePages.isFirst())
                .last(routePages.isLast())
                .list(Collections.singletonList(routes))
                .build();
        when(objectsMapperService.mapToSortedPaged(routes, routePages)).thenReturn(pagedSortedDto);
        PagedSortedDto pagedSortedDtoResult = routeService.getAllRoutes(pageNo, pageSize, sortBy, sortDir);
        verify(modelMapper, times(1)).map(route, RouteDto.class);
        assertEquals(pagedSortedDto.getTotalElements(), pagedSortedDtoResult.getTotalElements());
        assertEquals(pagedSortedDto.getList(), pagedSortedDtoResult.getList());
    }

    @Test
    void getAllRoutesWhichContainsStation() {
        int pageNo = 5, pageSize = 1;
        String sortBy = "id", sortDir = "asc";
        String expected = "Station not found with id: 1";
        Page<Route> routePages = new PageImpl<>(new ArrayList<>(List.of()));
        when(routeRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir))).thenReturn(routePages);
        try {
            routeService.getAllRoutesWhichContainsStation(pageNo, pageSize, sortBy, sortDir, 1L);
        }catch (ResourceNotFoundException e){
            verify(objectsMapperService, times(0)).mapToSortedPaged(new ArrayList<>(), routePages);
            verify(modelMapper, times(0)).map(route, RouteDto.class);
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void addStationToRoute() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(stationService.insertStation(stationDto)).thenReturn(stationDto);
        when(modelMapper.map(stationDto, Station.class)).thenReturn(station);
        when(routeRepository.save(route)).thenReturn(route);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        routeService.addStationToRoute(1L, stationDto);
        verify(routeRepository,times(1)).findById(1L);
        verify(modelMapper, times(1)).map(stationDto, Station.class);
        verify(modelMapper, times(1)).map(route, RouteDto.class);
    }

    @Test
    void addStationToRouteById() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(stationService.getStationById(1L)).thenReturn(stationDto);
        when(modelMapper.map(stationDto, Station.class)).thenReturn(station);
        when(routeRepository.save(route)).thenReturn(route);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        routeService.addStationToRouteById(1L, 1L);
        verify(routeRepository,times(1)).findById(1L);
        verify(stationService, times(1)).getStationById(1L);
        verify(modelMapper, times(1)).map(stationDto, Station.class);
        verify(modelMapper, times(1)).map(route, RouteDto.class);
    }

    @Test
    void removeStationFromRoute() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(stationService.getStationById(1L)).thenReturn(stationDto);
        when(modelMapper.map(stationDto, Station.class)).thenReturn(station);
        when(routeRepository.save(route)).thenReturn(route);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        routeService.removeStationFromRoute(1L, 1L);
        verify(routeRepository,times(1)).findById(1L);
        verify(stationService, times(1)).getStationById(1L);
        verify(modelMapper, times(1)).map(stationDto, Station.class);
        verify(modelMapper, times(1)).map(route, RouteDto.class);
        verify(routeRepository, times(1)).save(route);
    }

    @Test
    void getRouteByDestinationHappyFlow() {
        List<Route> routes = List.of(route);
        String nameDestination = "Something";
        when(routeRepository.findAllByToPlace(nameDestination)).thenReturn(Optional.of(routes));
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        routeService.getRouteByDestination(nameDestination);
        verify(modelMapper, times(1)).map(route, RouteDto.class);
    }

    @Test
    void getRouteByDestinationNegativeFlow() {
        List<Route> routes = List.of();
        String nameDestination = "Something";
        String expected = "Route not found with toPlace: " + nameDestination;
        when(routeRepository.findAllByToPlace(nameDestination)).thenReturn(Optional.of(routes));
        try{
            routeService.getRouteByDestination(nameDestination);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
            verify(modelMapper, times(0)).map(route, RouteDto.class);
        }
    }

    @Test
    void saveRouteAndMapToDto() {
        when(routeRepository.save(route)).thenReturn(route);
        when(modelMapper.map(route, RouteDto.class)).thenReturn(routeDto);
        RouteForBookingDto routeForBookingDtoResult = routeService.saveRouteAndMapToDto(route);
        assertEquals(routeDto.getFromPlace(), routeForBookingDtoResult.getFromPlace());
        verify(routeRepository, times(1)).save(route);
    }

    @Test
    void getRouteByIdAsRouteHappyFLow() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        Route routeResult = routeService.getRouteByIdAsRoute(1L);
        assertEquals(route.getToPlace(), routeResult.getToPlace());
        assertEquals(route.getStations(), routeResult.getStations());
    }

    @Test
    void getRouteByIdAsRouteNegativeFLow() {
        String expected = "Route not found with id: 1";
        when(routeRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Route", "id", 1L));
        try{
            routeService.getRouteByIdAsRoute(1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }
}