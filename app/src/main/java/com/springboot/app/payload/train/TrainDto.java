package com.springboot.app.payload.train;

import com.springboot.app.utils.validation.EstimatedTotalTimeValidation;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TrainDto extends TrainWithCompleteRouteDto{
    private Long id;
    @NotNull(message = "Number of setsFirstClass cannot be null")
    private Integer seatsFirstClass;
    @NotNull(message = "Number of setsSecondClass cannot be null")
    private Integer seatsSecondClass;
    @NotNull(message = "Number of compartmentsNumber cannot be null")
    private Integer compartmentsNumber;
    @EstimatedTotalTimeValidation
    private String estimatedTotalTime;
}
