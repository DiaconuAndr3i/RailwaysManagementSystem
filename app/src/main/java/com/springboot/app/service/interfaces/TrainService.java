package com.springboot.app.service.interfaces;

import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.train.TrainDto;
import com.springboot.app.payload.train.TrainWithCompleteRouteDto;

public interface TrainService {
    TrainWithCompleteRouteDto createTrain(TrainDto trainDto, Long idRoute);
    TrainWithCompleteRouteDto changeRouteForTrain(Long idRoute, Long idTrain);
    TrainWithCompleteRouteDto getTrainById(Long idTrain);
    String deleteTrain(Long idTrain);
    PagedSortedDto getAllTrains(int pageNo, int pageSize, String sortBy, String sortDir);
}
