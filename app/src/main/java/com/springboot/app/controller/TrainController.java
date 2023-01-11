package com.springboot.app.controller;

import com.springboot.app.payload.*;
import com.springboot.app.payload.train.TrainDto;
import com.springboot.app.payload.train.TrainWithCompleteRouteDto;
import com.springboot.app.service.interfaces.TrainService;
import com.springboot.app.utils.pagination.SortPageConst;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/train")
public class TrainController {
    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    @PostMapping(value = {"/route/{id}", ""})
    public ResponseEntity<TrainWithCompleteRouteDto> createTrain(@RequestBody @Valid TrainDto trainDto,
                                                                 @PathVariable(required = false) Long id){
        return new ResponseEntity<>(trainService.createTrain(trainDto, id), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TrainWithCompleteRouteDto> getTrainById(@PathVariable Long id){
        return new ResponseEntity<>(trainService.getTrainById(id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrain(@PathVariable Long id){
        return new ResponseEntity<>(trainService.deleteTrain(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PagedSortedDto> getAllTrains(
            @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(trainService.getAllTrains(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
    @PutMapping("/{idTrain}/route/{idRoute}")
    public ResponseEntity<TrainWithCompleteRouteDto> changeRouteForTrain(@PathVariable(name = "idTrain") Long idTrain,
                                                                    @PathVariable(name = "idRoute") Long idRoute){
        return new ResponseEntity<>(trainService.changeRouteForTrain(idRoute, idTrain), HttpStatus.CREATED);
    }
}
