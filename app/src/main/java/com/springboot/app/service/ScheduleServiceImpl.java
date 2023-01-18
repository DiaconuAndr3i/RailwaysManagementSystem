package com.springboot.app.service;

import com.springboot.app.entity.Schedule;
import com.springboot.app.entity.Train;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.schedule.ScheduleForBookingDto;
import com.springboot.app.repository.ScheduleRepository;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.ScheduleService;
import com.springboot.app.utils.pagination.SortPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ObjectsMapperService objectsMapperService;
    private final TrainRepository trainRepository;
    private final ModelMapper modelMapper;
    @Override
    public ScheduleForBookingDto getScheduleById(Long idSchedule) {
        return modelMapper.map(getScheduleByIdAsSchedule(idSchedule), ScheduleDto.class);
    }

    @Override
    public ScheduleForBookingDto createSchedule(ScheduleDto scheduleDto, Long idTrain) {
        Schedule schedule = modelMapper.map(scheduleDto, Schedule.class);
        Train train = trainRepository.findById(idTrain).orElseThrow(() -> new ResourceNotFoundException("Train", "id", idTrain));
        schedule.setTrain(train);
        return modelMapper.map(scheduleRepository.save(schedule), ScheduleDto.class);
    }

    @Override
    public ScheduleForBookingDto updateSchedule(Long idSchedule, ScheduleDto scheduleDto) {
        Schedule schedule = getScheduleByIdAsSchedule(idSchedule);
        schedule.setDepartureTime(scheduleDto.getDepartureTime());
        schedule.setArrivalTime(scheduleDto.getArrivalTime());
        schedule.setClassIAvailableSeats(scheduleDto.getClassIAvailableSeats());
        schedule.setClassIIAvailableSeats(scheduleDto.getClassIIAvailableSeats());
        return modelMapper.map(scheduleRepository.save(schedule), ScheduleDto.class);
    }

    @Override
    public ScheduleForBookingDto getScheduleByIdTrain(Long idTrain) {
        Schedule schedule = scheduleRepository.findByTrainId(idTrain).orElseThrow(() -> new ResourceNotFoundException("Schedule", "idTrain", idTrain));
        return modelMapper.map(schedule, ScheduleDto.class);
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
                .map(schedule -> modelMapper.map(schedule, ScheduleDto.class))
                .toList();
        return objectsMapperService.mapToSortedPaged(schedules, schedulePages);
    }

    @Override
    public List<ScheduleForBookingDto> getAllTrainsToDestination(String nameDestination) {
        List<ScheduleForBookingDto> list = scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getTrain().getRoute().getToPlace().contains(nameDestination))
                .map(schedule -> (ScheduleForBookingDto) modelMapper.map(schedule, ScheduleDto.class))
                .toList();
        if(list.size() == 0){
            throw new ResourceNotFoundException("Destination", "name", nameDestination);
        }
        return list;
    }

    @Override
    public List<ScheduleForBookingDto> getAllTrainsToDestinationAndStation(String nameDestination, Long idStation) {
        List<ScheduleForBookingDto> list = scheduleRepository.findAll()
                .stream()
                .filter(schedule -> schedule.getTrain().getRoute().getToPlace().contains(nameDestination))
                .filter(schedule -> schedule.getTrain().getRoute().stationsContains(idStation))
                .map(schedule -> (ScheduleForBookingDto) modelMapper.map(schedule, ScheduleDto.class))
                .toList();
        if(list.size() == 0){
            throw new ResourceNotFoundException("Destination and/or station", "name and/or id", nameDestination + " and/or " + idStation);
        }
        return list;
    }

    public Schedule getScheduleByIdAsSchedule(Long id){
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule", "id", id));
    }
}
