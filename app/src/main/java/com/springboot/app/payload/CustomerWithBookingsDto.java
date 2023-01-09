package com.springboot.app.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerWithBookingsDto extends CustomerDto{
    private List<BookingDto> bookings;
}
