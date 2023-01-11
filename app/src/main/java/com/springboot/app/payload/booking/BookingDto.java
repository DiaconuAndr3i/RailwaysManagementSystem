package com.springboot.app.payload.booking;

import com.springboot.app.payload.schedule.ScheduleForBookingDto;
import com.springboot.app.utils.validation.CompartmentValidation;
import com.springboot.app.utils.enums.Class;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;
    private Class trainClass;
    @NotEmpty(message = "Compartment cannot be null or empty")
    @CompartmentValidation
    private String compartment;
    @NotNull
    private Integer seat;
    private ScheduleForBookingDto schedule;
}
