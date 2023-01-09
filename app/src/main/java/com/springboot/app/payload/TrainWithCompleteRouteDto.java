package com.springboot.app.payload;

import com.springboot.app.utils.Type;
import lombok.Data;

@Data
public class TrainWithCompleteRouteDto {
    private Type trainType;
    private RouteDto route;
}
