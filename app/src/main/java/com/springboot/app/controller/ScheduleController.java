package com.springboot.app.controller;

import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.schedule.ScheduleForBookingDto;
import com.springboot.app.service.interfaces.ScheduleService;
import com.springboot.app.utils.SortPageConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    @PostMapping("/train/{idTrain}")
    public ResponseEntity<ScheduleForBookingDto> insertSchedule(@RequestBody ScheduleDto scheduleDto, @PathVariable Long idTrain){
        return new ResponseEntity<>(scheduleService.createSchedule(scheduleDto, idTrain), HttpStatus.CREATED);
    }
    @GetMapping("/{idSchedule}")
    public ResponseEntity<ScheduleForBookingDto> getScheduleById(@PathVariable(name = "idSchedule") Long id){
        return new ResponseEntity<>(scheduleService.getScheduleById(id), HttpStatus.OK);
    }
    @GetMapping("/destination/{name}")
    public ResponseEntity<List<ScheduleForBookingDto>> getSchedulesByDestination(@PathVariable(name = "name") String name){
        return new ResponseEntity<>(scheduleService.getAllTrainsToDestination(name), HttpStatus.OK);
    }
    @GetMapping("/destination/{name}/station/{id}")
    public ResponseEntity<List<ScheduleForBookingDto>> getSchedulesByDestinationAndStation(@PathVariable(name = "name") String name,
                                                                                           @PathVariable Long id){
        return new ResponseEntity<>(scheduleService.getAllTrainsToDestinationAndStation(name, id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PagedSortedDto> getAllSchedules(
            @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(scheduleService.getAllSchedules(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleForBookingDto> updateSchedule(@RequestBody ScheduleDto scheduleDto, @PathVariable Long id){
        return new ResponseEntity<>(scheduleService.updateSchedule(id, scheduleDto), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.deleteSchedule(id), HttpStatus.OK);
    }
}
