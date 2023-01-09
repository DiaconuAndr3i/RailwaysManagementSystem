package com.springboot.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class main {
    public static void main(String[] args) throws ParseException {
        /*final String OLD_FORMAT = "dd/MM/yyyy";
        final String NEW_FORMAT = "yyyy/MM/dd";

        // August 12, 2010
        String oldDateString = "12/08/2010";
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(oldDateString);
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);

        System.out.println(newDateString);*/

        final String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat();
        String date = "02-1-2018 06:07:59";
        sdf.applyPattern(pattern);
        Date d = sdf.parse(date);
        System.out.println(d);


    }
}
