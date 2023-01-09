package com.springboot.app.service.interfaces;

import com.springboot.app.payload.BookingDto;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long scheduleId, Long customerId);
}
