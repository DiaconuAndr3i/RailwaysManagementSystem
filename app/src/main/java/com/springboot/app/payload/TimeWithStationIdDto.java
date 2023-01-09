package com.springboot.app.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TimeWithStationIdDto extends TimeDto{
    private Long stationId;
}
