package com.springboot.app.controller;

import com.springboot.app.service.interfaces.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/time")
@RequiredArgsConstructor
public class TimeController {
    private final TimeService timeService;

}
