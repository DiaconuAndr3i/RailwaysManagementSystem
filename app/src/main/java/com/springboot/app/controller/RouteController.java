package com.springboot.app.controller;

import com.springboot.app.payload.*;
import com.springboot.app.payload.route.RouteDto;
import com.springboot.app.payload.route.RouteForBookingDto;
import com.springboot.app.payload.station.ListIdsStationsDto;
import com.springboot.app.payload.station.StationDto;
import com.springboot.app.service.interfaces.RouteService;
import com.springboot.app.utils.SortPageConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping
    public ResponseEntity<RouteForBookingDto> createRoute(@RequestBody RouteDto routeDto){
        return new ResponseEntity<>(routeService.createRoute(routeDto), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RouteForBookingDto> assignStationsToRoute(@PathVariable Long id,
                                                                    @RequestBody ListIdsStationsDto listIdsStationsDto){
        return new ResponseEntity<>(routeService.assignStationsToRoute(id, listIdsStationsDto), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RouteForBookingDto> getRouteById(@PathVariable Long id){
        return new ResponseEntity<>(routeService.getRouteById(id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable Long id){
        return new ResponseEntity<>(routeService.deleteRoute(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PagedSortedDto> getAllRoutes(
            @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(routeService.getAllRoutes(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    @GetMapping("/station/{id}")
    public ResponseEntity<PagedSortedDto> getAllRoutesWhichContainsStation(
            @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir,
            @PathVariable Long id
    ){
        return new ResponseEntity<>(routeService.getAllRoutesWhichContainsStation(pageNo, pageSize, sortBy, sortDir, id), HttpStatus.OK);
    }
    @PutMapping("/{id}/station")
    public ResponseEntity<RouteForBookingDto> addStationToRoute(@PathVariable(name = "id") Long idRoute,
                                                                @RequestBody StationDto stationDto){
        return new ResponseEntity<>(routeService.addStationToRoute(idRoute, stationDto), HttpStatus.CREATED);
    }
    @PutMapping("/{idRoute}/station/{idStation}")
    public ResponseEntity<RouteForBookingDto> addStationToRouteById(@PathVariable(name = "idRoute") Long idRoute,
                                                                    @PathVariable(name = "idStation") Long idStation){
        return new ResponseEntity<>(routeService.addStationToRouteById(idRoute, idStation), HttpStatus.CREATED);
    }
    @DeleteMapping("/{idRoute}/station/{idStation}")
    public ResponseEntity<RouteForBookingDto> removeStationFromRoute(@PathVariable(name = "idRoute") Long idRoute,
                                                                     @PathVariable(name = "idStation") Long idStation){
        return new ResponseEntity<>(routeService.removeStationFromRoute(idRoute, idStation), HttpStatus.OK);
    }
    @GetMapping("/destination")
    public ResponseEntity<List<RouteForBookingDto>> getRouteByDestination(@RequestParam(value = "name") String name){
        return new ResponseEntity<>(routeService.getRouteByDestination(name), HttpStatus.OK);
    }
}
