package com.springboot.app.payload.schedule;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleDto extends ScheduleForBookingDto {
    private Long id;
    @NotNull(message = "Available seats for class I cannot be null")
    private Integer classIAvailableSeats;
    @NotNull(message = "Available seats for class II cannot be null")
    private Integer classIIAvailableSeats;
}
