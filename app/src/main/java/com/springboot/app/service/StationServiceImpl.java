package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Station;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.station.StationDto;
import com.springboot.app.repository.RouteRepository;
import com.springboot.app.repository.StationRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.StationService;
import com.springboot.app.utils.pagination.SortPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;
    private final ObjectsMapperService objectsMapperService;
    private final RouteRepository routeRepository;
    private final ModelMapper modelMapper;
    @Override
    public StationDto insertStation(StationDto stationDto) {
        return modelMapper.map(stationRepository
                .save(modelMapper.map(stationDto, Station.class)), StationDto.class);
    }

    @Override
    public StationDto getStationById(Long id) {
        return modelMapper.map(getStationByIdAsStation(id), StationDto.class);
    }

    @Override
    public PagedSortedDto getAllStations(int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Station> stationPages = stationRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<StationDto> stations = stationPages.getContent()
                .stream()
                .map(station -> modelMapper.map(station, StationDto.class))
                .toList();
        return objectsMapperService.mapToSortedPaged(stations, stationPages);
    }

    @Override
    public StationDto getStationByName(String name) {
        Station station = stationRepository.getStationByName(name).orElseThrow(() -> new ResourceNotFoundException("Station", "name", name));
        return modelMapper.map(station, StationDto.class);
    }

    @Override
    public StationDto updateStation(StationDto stationDto, Long idStation) {
        Station station = getStationByIdAsStation(idStation);

        station.setCity(stationDto.getCity());
        station.setName(stationDto.getName());

        return modelMapper.map(stationRepository.save(station), StationDto.class);
    }

    @Override
    public String deleteStation(Long id) {
        Station station = getStationByIdAsStation(id);
        List<Route> routeList = routeRepository.findByStationsId(id).orElseThrow(() -> new ResourceNotFoundException("Route", "id", id));
        routeList.forEach(route -> route.removeStationFromRoute(station));
        routeRepository.saveAll(routeList);
        stationRepository.delete(station);
        return "Station deleted Successfully!";
    }


    public Station getStationByIdAsStation(Long id){
        return stationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station", "id", id));
    }
}
