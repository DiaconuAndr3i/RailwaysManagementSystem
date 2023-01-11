package com.springboot.app.controller;

import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.station.StationDto;
import com.springboot.app.service.interfaces.StationService;
import com.springboot.app.utils.SortPageConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/station")
@RequiredArgsConstructor
public class StationController {
    private final StationService stationService;
    @PostMapping
    public ResponseEntity<StationDto> insertStation(@RequestBody StationDto stationDto){
        return new ResponseEntity<>(stationService.insertStation(stationDto), HttpStatus.CREATED);
    }
    @GetMapping("/{idStation}")
    public ResponseEntity<StationDto> getStationById(@PathVariable(name = "idStation") Long id){
        return new ResponseEntity<>(stationService.getStationById(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PagedSortedDto> getAllStations(
            @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(stationService.getAllStations(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<StationDto> getStationByName(@PathVariable String name){
        return new ResponseEntity<>(stationService.getStationByName(name), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StationDto> updateStation(@RequestBody StationDto stationDto, @PathVariable Long id){
        return new ResponseEntity<>(stationService.updateStation(stationDto, id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStation(@PathVariable Long id){
        return new ResponseEntity<>(stationService.deleteStation(id), HttpStatus.OK);
    }
}
