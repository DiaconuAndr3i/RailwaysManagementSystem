package com.springboot.app.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TimeWithTrainAndStationDto extends TimeDto{
    private StationDto station;
    private TrainForBookingDto train;
}
