package com.springboot.app.service;

import com.springboot.app.entity.Schedule;
import com.springboot.app.entity.Train;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.schedule.ScheduleForBookingDto;
import com.springboot.app.repository.ScheduleRepository;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.ScheduleService;
import com.springboot.app.utils.SortPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ObjectsMapperService objectsMapperService;
    private final TrainRepository trainRepository;
    @Override
    public ScheduleForBookingDto getScheduleById(Long idSchedule) {
        return (ScheduleForBookingDto) objectsMapperService
                .mapFromTo(getScheduleByIdAsSchedule(idSchedule), new ScheduleDto());
    }

    @Override
    public ScheduleForBookingDto createSchedule(ScheduleDto scheduleDto, Long idTrain) {
        Schedule schedule = (Schedule) objectsMapperService.mapFromTo(scheduleDto, new Schedule());
        Train train = trainRepository.findById(idTrain).orElseThrow(RuntimeException::new);
        schedule.setTrain(train);
        return (ScheduleForBookingDto) objectsMapperService
                .mapFromTo(scheduleRepository.save(schedule), new ScheduleDto());
    }

    @Override
    public ScheduleForBookingDto updateSchedule(Long idSchedule, ScheduleDto scheduleDto) {
        Schedule schedule = getScheduleByIdAsSchedule(idSchedule);
        schedule.setDepartureTime(scheduleDto.getDepartureTime());
        schedule.setArrivalTime(scheduleDto.getArrivalTime());
        schedule.setClassIAvailableSeats(scheduleDto.getClassIAvailableSeats());
        schedule.setClassIIAvailableSeats(scheduleDto.getClassIIAvailableSeats());
        return (ScheduleForBookingDto) objectsMapperService.mapFromTo(scheduleRepository.save(schedule), new ScheduleDto());
    }

    @Override
    public ScheduleForBookingDto getScheduleByIdTrain(Long idTrain) {
        return (ScheduleForBookingDto) objectsMapperService
                .mapFromTo(scheduleRepository.findByTrainId(idTrain), new ScheduleDto());
    }

    @Override
    public String deleteSchedule(Long idSchedule) {
        scheduleRepository.delete(getScheduleByIdAsSchedule(idSchedule));
        return "Schedule was deleted successfully!";
    }

    @Override
    public PagedSortedDto getAllSchedules(int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Schedule> schedulePages = scheduleRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<ScheduleDto> schedules = schedulePages.getContent()
                .stream()
                .map(schedule -> (ScheduleDto) objectsMapperService.mapFromTo(schedule, new ScheduleDto()))
                .toList();
        return objectsMapperService.mapToSortedPaged(schedules, schedulePages);
    }

    @Override
    public List<ScheduleForBookingDto> getAllTrainsToDestination(String nameDestination) {
        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getTrain().getRoute().getToPlace().contains(nameDestination))
                .map(schedule -> (ScheduleForBookingDto) objectsMapperService.mapFromTo(schedule, new ScheduleDto()))
                .toList();
    }

    @Override
    public List<ScheduleForBookingDto> getAllTrainsToDestinationAndStation(String nameDestination, Long idStation) {
        return scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getTrain().getRoute().getToPlace().contains(nameDestination))
                .filter(schedule -> schedule.getTrain().getRoute().stationsContains(idStation))
                .map(schedule -> (ScheduleForBookingDto) objectsMapperService.mapFromTo(schedule, new ScheduleDto()))
                .toList();
    }

    public Schedule getScheduleByIdAsSchedule(Long id){
        return scheduleRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
