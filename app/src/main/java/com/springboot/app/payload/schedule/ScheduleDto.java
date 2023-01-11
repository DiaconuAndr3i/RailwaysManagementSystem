package com.springboot.app.payload.schedule;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleDto extends ScheduleForBookingDto {
    private Long id;
    private Integer classIAvailableSeats;
    private Integer classIIAvailableSeats;
}
