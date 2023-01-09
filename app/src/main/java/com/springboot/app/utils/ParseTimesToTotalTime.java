package com.springboot.app.utils;

import com.springboot.app.entity.Time;

import java.util.*;

public class ParseTimesToTotalTime {
    public static String parseTimes(Set<Time> times){
        int hours = 0, minutes = 0;
        for(Time time: times){
            hours += getTime(time.getValue(), Moment.HOURS);
            minutes += getTime(time.getValue(), Moment.MINUTES);
        }
        hours += minutes / 60;
        minutes = minutes % 60;
        return minutes < 10? hours +":0"+ minutes: hours +":"+ minutes;
    }

    public static Integer getTime(String time, Moment moment){

        if (moment == Moment.HOURS){
            return Integer.parseInt(List.of(time.split(":")).get(0));
        }
        return Integer.parseInt(List.of(time.split(":")).get(1));
    }
}
