package com.springboot.app.controller;

import com.springboot.app.payload.*;
import com.springboot.app.service.interfaces.RouteService;
import com.springboot.app.utils.SortPageConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
