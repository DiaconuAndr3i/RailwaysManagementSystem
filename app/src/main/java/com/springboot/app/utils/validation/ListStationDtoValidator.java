package com.springboot.app.utils.validation;

import com.springboot.app.payload.station.StationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class ListStationDtoValidator implements ConstraintValidator<ListStationDtoValidation, List<StationDto>> {
    @Override
    public boolean isValid(List<StationDto> stationDtoList, ConstraintValidatorContext constraintValidatorContext) {
        for(StationDto stationDto: stationDtoList){
            if(stationDto.getName().isEmpty() || stationDto.getCity().isEmpty())
                return false;
        }
        return true;
    }
}
