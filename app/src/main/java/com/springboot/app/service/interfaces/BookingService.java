package com.springboot.app.service.interfaces;

import com.springboot.app.payload.booking.BookingDto;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long scheduleId, Long customerId);
    BookingDto updateBooking(Long bookingId, BookingDto bookingDto);
    String deleteBooking(Long bookingId);
}
