package com.springboot.app.payload.customer;

import com.springboot.app.payload.booking.BookingDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerWithBookingsDto extends CustomerDto{
    private List<BookingDto> bookings;
}
