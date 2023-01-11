package com.springboot.app.payload.booking;

import com.springboot.app.payload.schedule.ScheduleForBookingDto;
import com.springboot.app.utils.Class;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    @NotEmpty(message = "Name class cannot be empty")
    private Class trainClass;
    @NotEmpty(message = "Name class cannot be empty")
    private String compartment;
    @NotEmpty(message = "Name class cannot be empty")
    private Integer seat;
    private ScheduleForBookingDto schedule;
}
