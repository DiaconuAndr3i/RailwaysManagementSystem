package com.springboot.app.payload;

import lombok.Data;

import java.util.Date;
@Data
public class ScheduleForBookingDto {
    private Date departureTime;
    private Date arrivalTime;
    TrainForBookingDto train;
}
