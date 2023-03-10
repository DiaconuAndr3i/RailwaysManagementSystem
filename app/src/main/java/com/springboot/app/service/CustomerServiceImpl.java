package com.springboot.app.service;

import com.springboot.app.entity.Booking;
import com.springboot.app.entity.Customer;
import com.springboot.app.entity.Schedule;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.booking.BookingDto;
import com.springboot.app.payload.customer.CustomerDto;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.repository.BookingRepository;
import com.springboot.app.repository.CustomerRepository;
import com.springboot.app.service.interfaces.BookingService;
import com.springboot.app.service.interfaces.CustomerService;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.ScheduleService;
import com.springboot.app.utils.pagination.SortPage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService, BookingService {

    private final CustomerRepository customerRepository;
    private final ObjectsMapperService objectsMapperService;
    private final BookingRepository bookingRepository;
    private final ScheduleService scheduleService;
    private final ModelMapper modelMapper;
    @Override
    public CustomerDto insertCustomer(CustomerDto customerDto) {
        return modelMapper.map(customerRepository
                .save(modelMapper.map(customerDto, Customer.class)), CustomerDto.class);
    }


    @Override
    public <T> PagedSortedDto getAllCustomersGeneric(int pageNo, int pageSize, String sortBy, String sortDir, T object) {
        Page<Customer> customerPages = customerRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<CustomerDto> listObject = customerPages.getContent()
                .stream()
                .map(customer -> (CustomerDto) modelMapper.map(customer, object.getClass()))
                .toList();
        return objectsMapperService.mapToSortedPaged(listObject, customerPages);
    }

    @Override
    public <T> CustomerDto getCustomerById(Long idCustomer, T object) {
        Customer customer = getCustomerById(idCustomer);
        return (CustomerDto) modelMapper.map(customer, object.getClass());
    }

    @Override
    public CustomerDto updateCustomer(Long idCustomer, CustomerDto customerDto) {
        Customer customer = getCustomerById(idCustomer);

        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());

        return modelMapper.map(customerRepository.save(customer), CustomerDto.class);
    }

    @Override
    public String deleteCustomer(Long idCustomer) {
        customerRepository.delete(getCustomerById(idCustomer));
        return "Customer was successfully deleted!";
    }

    public Customer getCustomerById(Long idCustomer){
        return customerRepository.findById(idCustomer).orElseThrow(() -> new ResourceNotFoundException("Customer", "id", idCustomer));
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto, Long scheduleId, Long customerId) {
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        Schedule schedule = scheduleService.getScheduleByIdAsSchedule(scheduleId);
        Customer customer = getCustomerById(customerId);
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        return saveAndMapToDto(booking);
    }

    @Override
    public BookingDto updateBooking(Long bookingId, BookingDto bookingDto) {
        Booking booking = getBookingById(bookingId);
        booking.setSeat(bookingDto.getSeat());
        booking.setCompartment(bookingDto.getCompartment());
        booking.setTrainClass(bookingDto.getTrainClass());
        return saveAndMapToDto(booking);
    }

    @Override
    public String deleteBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        bookingRepository.delete(booking);
        return "Booking was deleted successfully!";
    }

    public Booking getBookingById(Long idBooking){
        return bookingRepository.findById(idBooking).orElseThrow(() -> new ResourceNotFoundException("Booking", "id", idBooking));
    }

    public BookingDto saveAndMapToDto(Booking booking){
        return modelMapper.map(bookingRepository.save(booking), BookingDto.class);
    }
}
