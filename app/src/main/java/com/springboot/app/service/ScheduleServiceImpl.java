package com.springboot.app.service;

import com.springboot.app.entity.Schedule;
import com.springboot.app.payload.ScheduleDto;
import com.springboot.app.payload.ScheduleForBookingDto;
import com.springboot.app.repository.ScheduleRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ObjectsMapperService objectsMapperService;
    @Override
    public ScheduleForBookingDto getScheduleById(Long idSchedule) {
        return (ScheduleForBookingDto) objectsMapperService
                .mapFromTo(getScheduleByIdAsSchedule(idSchedule), new ScheduleDto());
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

    private Schedule getScheduleByIdAsSchedule(Long id){
        return scheduleRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
