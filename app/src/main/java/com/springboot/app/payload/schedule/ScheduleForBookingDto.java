package com.springboot.app.payload.schedule;

import com.springboot.app.payload.train.TrainDto;
import lombok.Data;

import java.util.Date;
@Data
public class ScheduleForBookingDto {
    private Date departureTime;
    private Date arrivalTime;
    TrainDto train;
}
