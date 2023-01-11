package com.springboot.app.service.interfaces;

import com.springboot.app.entity.Schedule;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.schedule.ScheduleForBookingDto;

import java.util.List;

public interface ScheduleService {
    ScheduleForBookingDto getScheduleById(Long idSchedule);
    ScheduleForBookingDto createSchedule(ScheduleDto scheduleDto, Long idTrain);
    ScheduleForBookingDto updateSchedule(Long idSchedule, ScheduleDto scheduleDto);
    ScheduleForBookingDto getScheduleByIdTrain(Long idTrain);
    String deleteSchedule(Long idSchedule);
    PagedSortedDto getAllSchedules(int pageNo, int pageSize, String sortBy, String sortDir);
    List<ScheduleForBookingDto> getAllTrainsToDestination(String nameDestination);
    List<ScheduleForBookingDto> getAllTrainsToDestinationAndStation(String nameDestination, Long idStation);
    Schedule getScheduleByIdAsSchedule(Long id);
}
