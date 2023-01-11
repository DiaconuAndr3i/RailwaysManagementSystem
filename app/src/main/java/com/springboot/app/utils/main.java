package com.springboot.app.utils;

import java.util.List;

public class main {
    public static void main(String[] args) {
        String str = "2:45";
        if(!str.matches("\\d+:\\d\\d")){
            System.out.println("Nu e bine");
        }
        List<String> list = List.of(str.split(":"));
        if(Integer.parseInt(list.get(1))>60){
            System.out.println("Nu e bine");
        }
    }
}
