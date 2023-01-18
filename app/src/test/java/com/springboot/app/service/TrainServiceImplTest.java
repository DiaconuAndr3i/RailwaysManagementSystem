package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Train;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.route.RouteForBookingDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.train.TrainDto;
import com.springboot.app.payload.train.TrainWithCompleteRouteDto;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.RouteService;
import com.springboot.app.service.interfaces.ScheduleService;
import com.springboot.app.utils.enums.Type;
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
class TrainServiceImplTest {
    @InjectMocks
    private TrainServiceImpl trainService;
    @Mock
    private TrainRepository trainRepository;
    @Mock
    private ObjectsMapperService objectsMapperService;
    @Mock
    private RouteService routeService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ModelMapper modelMapper;
    private final Train train;
    private final TrainDto trainDto;
    private final Route route;
    private final ScheduleDto scheduleDto;

    public TrainServiceImplTest(){
        train = Train.builder()
                .compartmentsNumber(4)
                .estimatedTotalTime("1:45")
                .seatsFirstClass(1)
                .seatsSecondClass(2)
                .trainType(Type.LIGHT)
                .build();

        trainDto = new TrainDto();
        trainDto.setTrainType(Type.LIGHT);
        trainDto.setCompartmentsNumber(6);
        trainDto.setEstimatedTotalTime("1:45");
        trainDto.setSeatsFirstClass(1);
        trainDto.setSeatsSecondClass(2);

        route = Route.builder()
                .id(1L)
                .fromPlace("CityRoute1")
                .toPlace("CityRoute2")
                .stations(new HashSet<>())
                .trains(new HashSet<>())
                .build();

        Date date1 = new Date();
        Date date2 = new Date();

        scheduleDto = new ScheduleDto();
        scheduleDto.setTrain(trainDto);
        scheduleDto.setClassIAvailableSeats(1);
        scheduleDto.setClassIIAvailableSeats(2);
        scheduleDto.setArrivalTime(date1);
        scheduleDto.setDepartureTime(date2);
    }

    @Test
    void createTrain() {
        when(modelMapper.map(trainDto, Train.class)).thenReturn(train);
        RouteForBookingDto routeForBookingDto = new RouteForBookingDto();
        when(routeService.getRouteById(1L)).thenReturn(routeForBookingDto);
        when(modelMapper.map(routeForBookingDto, Route.class)).thenReturn(route);
        when(trainRepository.save(train)).thenReturn(train);
        when(modelMapper.map(train, TrainDto.class)).thenReturn(trainDto);
        trainService.createTrain(trainDto, 1L);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void changeRouteForTrain() {
        route.setId(1L);
        train.setRoute(route);
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        RouteForBookingDto routeForBookingDto = new RouteForBookingDto();
        routeForBookingDto.setFromPlace("new City1");
        routeForBookingDto.setToPlace("new City2");
        when(routeService.getRouteById(2L)).thenReturn(routeForBookingDto);
        when(modelMapper.map(routeForBookingDto, Route.class)).thenReturn(route);
        when(trainRepository.save(train)).thenReturn(train);
        when(modelMapper.map(train, TrainDto.class)).thenReturn(trainDto);
        trainService.changeRouteForTrain(2L, 1L);
        verify(trainRepository, times(1)).save(train);
    }

    @Test
    void getTrainById() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(modelMapper.map(train, TrainDto.class)).thenReturn(trainDto);
        TrainWithCompleteRouteDto trainWithCompleteRouteDtoResult = trainService.getTrainById(1L);
        assertEquals(train.getTrainType(), trainWithCompleteRouteDtoResult.getTrainType());
        verify(trainRepository, times(1)).findById(1L);
    }

    @Test
    void deleteTrainFirstFlow() {
        String expected = "Train was deleted successfully!";
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(scheduleService.getScheduleByIdTrain(1L)).thenReturn(scheduleDto);
        String result = trainService.deleteTrain(1L);
        verify(scheduleService, times(1)).deleteSchedule(scheduleDto.getId());
        verify(trainRepository, times(1)).delete(train);
        assertEquals(expected, result);
    }

    @Test
    void deleteTrainSecondFlow() {
        String expected = "Train was deleted successfully!";
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(scheduleService.getScheduleByIdTrain(1L)).thenReturn(null);
        String result = trainService.deleteTrain(1L);
        verify(scheduleService, times(0)).deleteSchedule(any());
        verify(trainRepository, times(1)).delete(train);
        assertEquals(expected, result);
    }

    @Test
    void getAllTrains() {
        int pageNo = 5, pageSize = 1;
        String sortBy = "id", sortDir = "asc";
        Page<Train> trainPages = new PageImpl<>(new ArrayList<>(List.of(train)));
        when(trainRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir))).thenReturn(trainPages);
        when(modelMapper.map(train, TrainDto.class)).thenReturn(trainDto);
        List<TrainWithCompleteRouteDto> trains = List.of(trainDto);
        PagedSortedDto pagedSortedDto = PagedSortedDto.builder()
                .pageNo(trainPages.getNumber())
                .totalElements(trainPages.getTotalElements())
                .pageSize(trainPages.getSize())
                .totalPages(trainPages.getTotalPages())
                .first(trainPages.isFirst())
                .last(trainPages.isLast())
                .list(Collections.singletonList(trains))
                .build();
        when(objectsMapperService.mapToSortedPaged(trains, trainPages)).thenReturn(pagedSortedDto);
        PagedSortedDto pagedSortedDtoResult = trainService.getAllTrains(pageNo, pageSize, sortBy, sortDir);
        verify(modelMapper, times(1)).map(train, TrainDto.class);
        assertEquals(pagedSortedDto.getTotalElements(), pagedSortedDtoResult.getTotalElements());
        assertEquals(pagedSortedDto.getList(), pagedSortedDtoResult.getList());
    }

    @Test
    void getTrainByIdAsTrainHappyFLow() {
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Train trainResult = trainService.getTrainByIdAsTrain(1L);
        assertEquals(train.getCompartmentsNumber(), trainResult.getCompartmentsNumber());
        assertEquals(train.getSeatsFirstClass(), trainResult.getSeatsFirstClass());
    }

    @Test
    void getTrainByIdAsTrainNegativeFLow() {
        String expected = "Train not found with id: 1";
        when(trainRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Train", "id", 1L));
        try{
            trainService.getTrainByIdAsTrain(1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void saveTrainAndMapToDto() {
        when(trainRepository.save(train)).thenReturn(train);
        when(modelMapper.map(train, TrainDto.class)).thenReturn(trainDto);
        TrainWithCompleteRouteDto trainWithCompleteRouteDtoResult = trainService.saveTrainAndMapToDto(train);
        assertEquals(trainDto.getTrainType(), trainWithCompleteRouteDtoResult.getTrainType());
        verify(trainRepository, times(1)).save(train);
    }
}