package com.springboot.app.service;

import com.springboot.app.payload.BookingDto;
import com.springboot.app.repository.BookingRepository;
import com.springboot.app.service.interfaces.BookingService;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ObjectsMapperService objectsMapperService;
    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long scheduleId, Long customerId) {
        return null;
    }
}
