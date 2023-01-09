package com.springboot.app.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainDto extends TrainWithCompleteRouteDto{
    private Long id;
    private Integer seatsFirstClass;
    private Integer seatsSecondClass;
    private Integer compartmentsNumber;
    private String estimatedTotalTime;
}
