package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Station;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.route.RouteDto;
import com.springboot.app.payload.station.StationDto;
import com.springboot.app.repository.RouteRepository;
import com.springboot.app.repository.StationRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
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
class StationServiceImplTest {
    @InjectMocks
    private StationServiceImpl stationService;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private ObjectsMapperService objectsMapperService;
    @Mock
    private RouteRepository routeRepository;
    @Mock
    private ModelMapper modelMapper;

    private final Route route;
    private final StationDto stationDto;
    private final Station station;

    public StationServiceImplTest(){
        station = new Station();
        station.setId(1L);
        station.setName("Station");
        station.setCity("City");

        route = Route.builder()
                .id(1L)
                .fromPlace("CityRoute1")
                .toPlace("CityRoute2")
                .stations(new HashSet<>())
                .trains(new HashSet<>())
                .build();

        RouteDto routeDto = new RouteDto();
        routeDto.setFromPlace("CityRoute1");
        routeDto.setToPlace("CityRoute2");

        stationDto = new StationDto();
        stationDto.setName("Station");
        stationDto.setCity("City");
    }

    @Test
    void insertStation() {
        when(modelMapper.map(stationDto, Station.class)).thenReturn(station);
        when(stationRepository.save(station)).thenReturn(station);
        when(modelMapper.map(station, StationDto.class)).thenReturn(stationDto);
        StationDto stationDtoResult = stationService.insertStation(stationDto);
        assertEquals(stationDto.getCity(), stationDtoResult.getCity());
        verify(modelMapper, times(1)).map(stationDto, Station.class);
    }

    @Test
    void getStationById() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(modelMapper.map(station, StationDto.class)).thenReturn(stationDto);
        StationDto stationDtoResult = stationService.getStationById(1L);
        assertEquals(stationDto.getCity(), stationDtoResult.getCity());
        verify(stationRepository, times(1)).findById(1L);
    }

    @Test
    void getAllStations() {
        int pageNo = 5, pageSize = 1;
        String sortBy = "id", sortDir = "asc";
        Page<Station> stationPages = new PageImpl<>(new ArrayList<>(List.of(station)));
        when(stationRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir))).thenReturn(stationPages);
        when(modelMapper.map(station, StationDto.class)).thenReturn(stationDto);
        List<StationDto> stations = List.of(stationDto);
        PagedSortedDto pagedSortedDto = PagedSortedDto.builder()
                .pageNo(stationPages.getNumber())
                .totalElements(stationPages.getTotalElements())
                .pageSize(stationPages.getSize())
                .totalPages(stationPages.getTotalPages())
                .first(stationPages.isFirst())
                .last(stationPages.isLast())
                .list(Collections.singletonList(stations))
                .build();
        when(objectsMapperService.mapToSortedPaged(stations, stationPages)).thenReturn(pagedSortedDto);
        PagedSortedDto pagedSortedDtoResult = stationService.getAllStations(pageNo, pageSize, sortBy, sortDir);
        verify(modelMapper, times(1)).map(station, StationDto.class);
        assertEquals(pagedSortedDto.getTotalElements(), pagedSortedDtoResult.getTotalElements());
        assertEquals(pagedSortedDto.getList(), pagedSortedDtoResult.getList());
    }

    @Test
    void getStationByNameHappyFlow() {
        when(stationRepository.getStationByName("something")).thenReturn(Optional.of(station));
        when(modelMapper.map(station, StationDto.class)).thenReturn(stationDto);
        StationDto stationDtoResult = stationService.getStationByName("something");
        assertEquals(stationDto.getCity(), stationDtoResult.getCity());
        verify(modelMapper, times(1)).map(station, StationDto.class);
    }

    @Test
    void getStationByNameNegativeFlow() {
        String expected = "Station not found with name: something";
        when(stationRepository.getStationByName("something")).thenThrow(new ResourceNotFoundException("Station", "name", "something"));
        try{
            stationService.getStationByName("something");
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
            verify(modelMapper, times(0)).map(station, StationDto.class);
        }
    }

    @Test
    void updateStation() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(stationRepository.save(station)).thenReturn(station);
        when(modelMapper.map(station, StationDto.class)).thenReturn(stationDto);
        StationDto stationDtoResult = stationService.updateStation(stationDto, 1L);
        assertEquals(stationDto.getCity(), stationDtoResult.getCity());
        assertEquals(stationDto.getName(), stationDtoResult.getName());
        verify(stationRepository, times(1)).save(station);
    }

    @Test
    void deleteStationHappyFlow() {
        String expected = "Station deleted Successfully!";
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        List<Route> routeList = new ArrayList<>(List.of(route));
        when(routeRepository.findByStationsId(1L)).thenReturn(Optional.of(routeList));
        String result = stationService.deleteStation(1L);
        verify(routeRepository, times(1)).saveAll(routeList);
        verify(stationRepository, times(1)).delete(station);
        assertEquals(expected, result);
    }

    @Test
    void deleteStationNegativeFlow() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(routeRepository.findByStationsId(1L)).thenThrow(new ResourceNotFoundException("Route", "id", 1L));
        try {
            stationService.deleteStation(1L);
        }catch (ResourceNotFoundException e){
            assertEquals("Route not found with id: 1", e.getMessage());
            verify(routeRepository, times(0)).saveAll(any());
            verify(stationRepository, times(0)).delete(station);
        }
    }

    @Test
    void getStationByIdAsStationHappyFlow() {
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        Station stationResult = stationService.getStationByIdAsStation(1L);
        assertEquals(station.getName(), stationResult.getName());
    }

    @Test
    void getStationByIdAsStationNegativeFlow() {
        when(stationRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Station", "id", 1L));
        try{
            stationService.getStationByIdAsStation(1L);
        }catch (ResourceNotFoundException e){
            assertEquals("Station not found with id: 1", e.getMessage());
        }
    }
}