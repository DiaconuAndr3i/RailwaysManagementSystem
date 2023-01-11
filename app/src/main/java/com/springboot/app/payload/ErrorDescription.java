package com.springboot.app.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
public class ErrorDescription {
    private Date date;
    private String message;
    private String details;
}
