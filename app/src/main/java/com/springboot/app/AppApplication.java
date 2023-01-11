package com.springboot.app;

import com.springboot.app.entity.*;
import com.springboot.app.repository.*;
import com.springboot.app.utils.enums.Class;
import com.springboot.app.utils.enums.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@RequiredArgsConstructor
public class AppApplication implements CommandLineRunner {
    private final RouteRepository routeRepository;
    private final TrainRepository trainRepository;
    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final StationRepository stationRepository;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Route route = Route.builder()
                .fromPlace("Bucuresti")
                .toPlace("Brasov")
                .build();
        Route routeDB = routeRepository.save(route);

        Train train = Train.builder()
                .trainType(Type.RAPID)
                .seatsFirstClass(70)
                .seatsSecondClass(150)
                .compartmentsNumber(5)
                .estimatedTotalTime("1:45")
                .route(routeDB)
                .build();
        Train trainDB = trainRepository.save(train);

        Train train2 = Train.builder()
                .trainType(Type.RAPID)
                .seatsFirstClass(70)
                .seatsSecondClass(150)
                .compartmentsNumber(5)
                .estimatedTotalTime("1:45")
                .route(routeDB)
                .build();
        trainRepository.save(train2);

        final String pattern = "dd-M-yyyy hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat();
        String dateDeparture = "02-1-2023 06:15:00";
        String dateArrival = "02-1-2023 08:15:00";
        sdf.applyPattern(pattern);
        Date departureTime = sdf.parse(dateDeparture);
        Date arrivalTime = sdf.parse(dateArrival);
        Schedule schedule = Schedule.builder()
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .classIAvailableSeats(20)
                .classIIAvailableSeats(30)
                .train(trainDB)
                .build();
        Schedule scheduleDB = scheduleRepository.save(schedule);

        Customer customer = Customer.builder()
                .firstName("Andrei")
                .lastName("Diaconu")
                .email("diaconu.andrei99@gmail.com")
                .build();
        Customer customerDB = customerRepository.save(customer);

        Booking booking1 = Booking.builder()
                .trainClass(Class.CLASS_I)
                .compartment("C1")
                .seat(14)
                .customer(customerDB)
                .schedule(scheduleDB)
                .build();
        Booking booking2 = Booking.builder()
                .trainClass(Class.CLASS_II)
                .compartment("C3")
                .seat(15)
                .customer(customerDB)
                .schedule(scheduleDB)
                .build();
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);


        Station station1 = Station.builder()
                .city("Bucuresti")
                .name("Bucuresti 2")
                .build();
        Station station2 = Station.builder()
                .city("Ploiesti")
                .name("Ploiesti 2")
                .build();
        Station station3 = Station.builder()
                .city("Brasov")
                .name("Brasov 2")
                .build();
        stationRepository.save(station1);
        stationRepository.save(station2);
        stationRepository.save(station3);
    }
}
