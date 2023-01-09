package com.springboot.app.payload;

import com.springboot.app.utils.Type;
import lombok.Data;

@Data
public class TrainForBookingDto {
    private Type trainType;
    private RouteForBookingDto route;
}
