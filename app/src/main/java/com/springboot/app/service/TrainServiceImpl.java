package com.springboot.app.service;

import com.springboot.app.entity.Route;
import com.springboot.app.entity.Train;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.*;
import com.springboot.app.payload.route.RouteForBookingDto;
import com.springboot.app.payload.schedule.ScheduleDto;
import com.springboot.app.payload.train.TrainDto;
import com.springboot.app.payload.train.TrainWithCompleteRouteDto;
import com.springboot.app.repository.TrainRepository;
import com.springboot.app.service.interfaces.*;
import com.springboot.app.utils.pagination.SortPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;
    private final ObjectsMapperService objectsMapperService;
    private final RouteService routeService;
    private final ScheduleService scheduleService;
    private final ModelMapper modelMapper;
    @Override
    public TrainWithCompleteRouteDto createTrain(TrainDto trainDto, Long idRoute) {
        Train train = modelMapper.map(trainDto, Train.class);
        if(idRoute != null){
            RouteForBookingDto routeForBookingDto = routeService.getRouteById(idRoute);
            train.setRoute(modelMapper.map(routeForBookingDto, Route.class));
        }
        return saveTrainAndMapToDto(train);
    }

    @Override
    public TrainWithCompleteRouteDto changeRouteForTrain(Long idRoute, Long idTrain) {
        Train train = getTrainByIdAsTrain(idTrain);
        RouteForBookingDto routeForBookingDto = routeService.getRouteById(idRoute);
        train.setRoute(modelMapper.map(routeForBookingDto, Route.class));
        return saveTrainAndMapToDto(train);
    }

    @Override
    public TrainWithCompleteRouteDto getTrainById(Long idTrain) {
        return modelMapper.map(getTrainByIdAsTrain(idTrain), TrainDto.class);
    }

    @Override
    public String deleteTrain(Long idTrain) {
        Train train = getTrainByIdAsTrain(idTrain);
        ScheduleDto scheduleForBookingDto = (ScheduleDto) scheduleService.getScheduleByIdTrain(idTrain);
        if(scheduleForBookingDto != null){
            scheduleService.deleteSchedule(scheduleForBookingDto.getId());
        }
        trainRepository.delete(train);
        return "Train was deleted successfully!";
    }

    @Override
    public PagedSortedDto getAllTrains(int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Train> trainPages = trainRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<TrainWithCompleteRouteDto> trains = trainPages.getContent()
                .stream()
                .map(train -> (TrainWithCompleteRouteDto) modelMapper.map(train, TrainDto.class))
                .toList();
        return objectsMapperService.mapToSortedPaged(trains, trainPages);
    }

    public Train getTrainByIdAsTrain(Long id){
        return trainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train", "id", id));
    }
    public TrainWithCompleteRouteDto saveTrainAndMapToDto(Train train){
        return modelMapper.map(trainRepository.save(train), TrainDto.class);
    }
}
