package com.springboot.app.payload.train;

import com.springboot.app.payload.route.RouteDto;
import com.springboot.app.utils.Type;
import lombok.Data;

@Data
public class TrainWithCompleteRouteDto {
    private Type trainType;
    private RouteDto route;
}
