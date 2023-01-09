package com.springboot.app.payload;

import com.springboot.app.utils.Class;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    private Class trainClass;
    private String compartment;
    private Integer seat;
    private ScheduleForBookingDto schedule;
}
