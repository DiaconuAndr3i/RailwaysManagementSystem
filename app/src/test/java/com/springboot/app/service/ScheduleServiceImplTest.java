package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Schedule;
import com.springboot.app.entity.Station;
import com.springboot.app.entity.Train;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.schedule.ScheduleForBookingDto;
import com.springboot.app.payload.train.TrainDto;
import com.springboot.app.repository.ScheduleRepository;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
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
class ScheduleServiceImplTest {
    @InjectMocks
    private ScheduleServiceImpl scheduleService;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private ObjectsMapperService objectsMapperService;
    @Mock
    private TrainRepository trainRepository;
    @Mock
    private  ModelMapper modelMapper;
    private final Train train;
    private final Schedule schedule;
    private final ScheduleDto scheduleDto;

    public ScheduleServiceImplTest(){
        train = Train.builder()
                .compartmentsNumber(4)
                .estimatedTotalTime("1:45")
                .seatsFirstClass(1)
                .seatsSecondClass(2)
                .trainType(Type.LIGHT)
                .build();

        TrainDto trainDto = new TrainDto();
        trainDto.setTrainType(Type.LIGHT);
        trainDto.setCompartmentsNumber(6);
        trainDto.setEstimatedTotalTime("1:45");
        trainDto.setSeatsFirstClass(1);
        trainDto.setSeatsSecondClass(2);

        Date date1 = new Date();
        Date date2 = new Date();

        scheduleDto = new ScheduleDto();
        scheduleDto.setTrain(trainDto);
        scheduleDto.setClassIAvailableSeats(1);
        scheduleDto.setClassIIAvailableSeats(2);
        scheduleDto.setArrivalTime(date1);
        scheduleDto.setDepartureTime(date2);

        schedule = Schedule.builder()
                .train(train)
                .arrivalTime(date1)
                .departureTime(date2)
                .classIAvailableSeats(1)
                .classIIAvailableSeats(2)
                .build();
    }

    @Test
    void getScheduleById() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(modelMapper.map(schedule, ScheduleDto.class)).thenReturn(scheduleDto);
        ScheduleForBookingDto scheduleForBookingDtoResult = scheduleService.getScheduleById(1L);
        assertEquals(scheduleDto.getDepartureTime(), scheduleForBookingDtoResult.getDepartureTime());
        verify(scheduleRepository, times(1)).findById(1L);
    }

    @Test
    void createScheduleHappyFlow() {
        when(modelMapper.map(scheduleDto, Schedule.class)).thenReturn(schedule);
        when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        when(modelMapper.map(schedule, ScheduleDto.class)).thenReturn(scheduleDto);
        ScheduleForBookingDto scheduleForBookingDtoResult = scheduleService.createSchedule(scheduleDto, 1L);
        assertEquals(scheduleDto.getTrain(), scheduleForBookingDtoResult.getTrain());
        verify(scheduleRepository, times(1)).save(schedule);
    }

    @Test
    void createScheduleNegativeFlow() {
        String expected = "Train not found with id: 1";
        when(modelMapper.map(scheduleDto, Schedule.class)).thenReturn(schedule);
        when(trainRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Train", "id", 1L));
        try{
            scheduleService.createSchedule(scheduleDto, 1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
            verify(scheduleRepository, times(0)).save(schedule);
        }
    }

    @Test
    void updateSchedule() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(scheduleRepository.save(schedule)).thenReturn(schedule);
        when(modelMapper.map(schedule, ScheduleDto.class)).thenReturn(scheduleDto);
        ScheduleForBookingDto scheduleForBookingDtoResult = scheduleService.updateSchedule(1L, scheduleDto);
        assertEquals(scheduleDto.getArrivalTime(), scheduleForBookingDtoResult.getArrivalTime());
    }

    @Test
    void getScheduleByIdTrainHappyFlow() {
        when(scheduleRepository.findByTrainId(1L)).thenReturn(Optional.of(schedule));
        when(modelMapper.map(schedule, ScheduleDto.class)).thenReturn(scheduleDto);
        ScheduleForBookingDto scheduleForBookingDtoResult = scheduleService.getScheduleByIdTrain(1L);
        assertEquals(scheduleDto.getDepartureTime(), scheduleForBookingDtoResult.getDepartureTime());
    }

    @Test
    void getScheduleByIdTrainNegativeFlow() {
        String expected = "Schedule not found with idTrain: 1";
        when(scheduleRepository.findByTrainId(1L)).thenThrow(new ResourceNotFoundException("Schedule", "idTrain", 1L));
        try{
            scheduleService.getScheduleByIdTrain(1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
            verify(modelMapper, times(0)).map(schedule, ScheduleDto.class);
        }
    }

    @Test
    void deleteSchedule() {
        String expected = "Schedule was deleted successfully!";
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        String result = scheduleService.deleteSchedule(1L);
        verify(scheduleRepository, times(1)).delete(schedule);
        assertEquals(expected, result);
    }

    @Test
    void getAllSchedules() {
        int pageNo = 5, pageSize = 1;
        String sortBy = "id", sortDir = "asc";
        Page<Schedule> schedulePages = new PageImpl<>(new ArrayList<>(List.of(schedule)));
        when(scheduleRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir))).thenReturn(schedulePages);
        when(modelMapper.map(schedule, ScheduleDto.class)).thenReturn(scheduleDto);
        List<ScheduleDto> schedules = List.of(scheduleDto);
        PagedSortedDto pagedSortedDto = PagedSortedDto.builder()
                .pageNo(schedulePages.getNumber())
                .totalElements(schedulePages.getTotalElements())
                .pageSize(schedulePages.getSize())
                .totalPages(schedulePages.getTotalPages())
                .first(schedulePages.isFirst())
                .last(schedulePages.isLast())
                .list(Collections.singletonList(schedules))
                .build();
        when(objectsMapperService.mapToSortedPaged(schedules, schedulePages)).thenReturn(pagedSortedDto);
        PagedSortedDto pagedSortedDtoResult = scheduleService.getAllSchedules(pageNo, pageSize, sortBy, sortDir);
        verify(modelMapper, times(1)).map(schedule, ScheduleDto.class);
        assertEquals(pagedSortedDto.getTotalElements(), pagedSortedDtoResult.getTotalElements());
        assertEquals(pagedSortedDto.getList(), pagedSortedDtoResult.getList());
    }

    @Test
    void getAllTrainsToDestinationHappyFlow() {
        String nameDestination = "Something";
        List<ScheduleForBookingDto> listScheduleForBookingsDto = new ArrayList<>(List.of(scheduleDto));
        Route routeForThisTest = Route.builder()
                .toPlace(nameDestination)
                .build();
        Train trainFowThisTest = Train.builder()
                .route(routeForThisTest)
                .build();
        Schedule scheduleForThisTest = Schedule.builder()
                .train(trainFowThisTest)
                .build();
        List<Schedule> listSchedules = new ArrayList<>(List.of(scheduleForThisTest));
        when(scheduleRepository.findAll()).thenReturn(listSchedules);
        when(modelMapper.map(scheduleForThisTest, ScheduleDto.class)).thenReturn(scheduleDto);
        List<ScheduleForBookingDto> scheduleForBookingDtoResult = scheduleService.getAllTrainsToDestination(nameDestination);
        assertEquals(listScheduleForBookingsDto.get(0).getArrivalTime(), scheduleForBookingDtoResult.get(0).getArrivalTime());
        assertEquals(listScheduleForBookingsDto.get(0).getTrain().getTrainType(), scheduleForBookingDtoResult.get(0).getTrain().getTrainType());
    }

    @Test
    void getAllTrainsToDestinationNegativeFlow() {
        String nameDestination = "Something";
        String expected = "Destination not found with name: " + nameDestination;
        List<Schedule> listSchedules = new ArrayList<>(List.of());
        when(scheduleRepository.findAll()).thenReturn(listSchedules);
        try{
            scheduleService.getAllTrainsToDestination(nameDestination);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void getAllTrainsToDestinationAndStationHappyFLow() {
        String nameDestination = "Something";
        Long idStation = 1L;
        List<ScheduleForBookingDto> listScheduleForBookingsDto = new ArrayList<>(List.of(scheduleDto));
        Station stationForThisTest = Station.builder()
                .id(idStation)
                .build();
        Route routeForThisTest = Route.builder()
                .toPlace(nameDestination)
                .stations(Set.of(stationForThisTest))
                .build();
        Train trainFowThisTest = Train.builder()
                .route(routeForThisTest)
                .build();
        Schedule scheduleForThisTest = Schedule.builder()
                .train(trainFowThisTest)
                .build();
        List<Schedule> listSchedules = new ArrayList<>(List.of(scheduleForThisTest));
        when(scheduleRepository.findAll()).thenReturn(listSchedules);
        when(modelMapper.map(scheduleForThisTest, ScheduleDto.class)).thenReturn(scheduleDto);
        List<ScheduleForBookingDto> scheduleForBookingDtoResult = scheduleService.getAllTrainsToDestinationAndStation(nameDestination, idStation);
        assertEquals(listScheduleForBookingsDto.get(0).getArrivalTime(), scheduleForBookingDtoResult.get(0).getArrivalTime());
        assertEquals(listScheduleForBookingsDto.get(0).getTrain().getTrainType(), scheduleForBookingDtoResult.get(0).getTrain().getTrainType());
    }

    @Test
    void getAllTrainsToDestinationAndStationNegativeFLow() {
        String nameDestination = "Something";
        long idStation = 1L;
        String expected = "Destination and/or station not found with name and/or id: " + nameDestination + " and/or " + idStation;
        List<Schedule> listSchedules = new ArrayList<>(List.of());
        when(scheduleRepository.findAll()).thenReturn(listSchedules);
        try{
            scheduleService.getAllTrainsToDestinationAndStation(nameDestination, idStation);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void getScheduleByIdAsScheduleHappyFLow() {
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        Schedule scheduleResult = scheduleService.getScheduleByIdAsSchedule(1L);
        assertEquals(schedule.getClassIIAvailableSeats(), scheduleResult.getClassIIAvailableSeats());
    }

    @Test
    void getScheduleByIdAsScheduleNegativeFLow() {
        String expected = "Schedule not found with id: 1";
        when(scheduleRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Schedule", "id", 1L));

        try{
            scheduleService.getScheduleByIdAsSchedule(1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }
}