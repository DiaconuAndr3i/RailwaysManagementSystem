package com.springboot.app.payload;

import lombok.Data;

@Data
public class TimeDto {
    private Long id;
    private String value;
    private Long beforeStatId;
    private Long afterStatId;
}
