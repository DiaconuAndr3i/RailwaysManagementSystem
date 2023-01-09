package com.springboot.app.service.interfaces;

import com.springboot.app.payload.ScheduleForBookingDto;

public interface ScheduleService {
    ScheduleForBookingDto getScheduleById(Long idSchedule);
    ScheduleForBookingDto getScheduleByIdTrain(Long idTrain);
    String deleteSchedule(Long idSchedule);
}
