package com.springboot.app.service;

import com.springboot.app.entity.Booking;
import com.springboot.app.entity.Customer;
import com.springboot.app.entity.Schedule;
import com.springboot.app.exception.ResourceNotFoundException;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.payload.booking.BookingDto;
import com.springboot.app.payload.customer.CustomerDto;
import com.springboot.app.repository.BookingRepository;
import com.springboot.app.repository.CustomerRepository;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.service.interfaces.ScheduleService;
import com.springboot.app.utils.enums.Class;
import com.springboot.app.utils.pagination.SortPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ObjectsMapperService objectsMapperService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ModelMapper modelMapper;

    private final Customer customer;
    private final CustomerDto customerDto;
    private final Booking booking;
    private final BookingDto bookingDto;
    private final Schedule schedule;
    public CustomerServiceImplTest(){
        customer = new Customer();
        customer.setFirstName("FirstName");
        customer.setLastName("LastName");
        customer.setEmail("email");
        customerDto = new CustomerDto();
        customerDto.setFirstName("FirstName");
        customerDto.setLastName("LastName");
        customerDto.setEmail("email");
        bookingDto = new BookingDto();
        bookingDto.setCompartment("C1");
        bookingDto.setSeat(10);
        bookingDto.setTrainClass(Class.CLASS_I);
        booking = new Booking();
        booking.setTrainClass(Class.CLASS_I);
        booking.setSeat(10);
        booking.setCompartment("C1");
        schedule = new Schedule();
        schedule.setDepartureTime(new Date());
        schedule.setArrivalTime(new Date());
        schedule.setClassIAvailableSeats(10);
        schedule.setClassIIAvailableSeats(20);
    }

    @Test
    void insertCustomer() {
        when(modelMapper.map(customerDto, Customer.class)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
        CustomerDto customerDtoResult = customerService.insertCustomer(customerDto);
        verify(customerRepository, times(1)).save(customer);
        assertEquals(customerDto.getEmail(), customerDtoResult.getEmail());
    }

    @Test
    void getAllCustomersGeneric() {
        int pageNo=0, pageSize=5;
        String sortBy="id", sortDir="asc";
        Page<Customer> customerPages = new PageImpl<>(new ArrayList<>(List.of(customer)));
        when(customerRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir))).thenReturn(customerPages);
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
        List<CustomerDto> listObject = List.of(customerDto);
        PagedSortedDto pagedSortedDto = new PagedSortedDto(pageNo,pageSize, customerPages.getTotalElements(),
                customerPages.getTotalPages(),customerPages.isFirst(),customerPages.isLast(), Collections.singletonList(listObject));
        when(objectsMapperService.mapToSortedPaged(listObject, customerPages)).thenReturn(pagedSortedDto);
        PagedSortedDto pagedSortedDtoResult = customerService.getAllCustomersGeneric(pageNo, pageSize, sortBy, sortDir, new CustomerDto());
        assertEquals(pagedSortedDto.getPageNo(), pagedSortedDtoResult.getPageNo());
        assertEquals(pagedSortedDto.getList(), pagedSortedDtoResult.getList());
        assertEquals(pagedSortedDto.getTotalElements(), pagedSortedDtoResult.getTotalElements());
    }

    @Test
    void getCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
        CustomerDto customerDtoResult = customerService.getCustomerById(1L, new CustomerDto());
        assertEquals(customerDto.getLastName(), customerDtoResult.getLastName());
        assertEquals(customerDto.getFirstName(), customerDtoResult.getFirstName());
        assertEquals(customerDto.getId(), customerDtoResult.getId());
    }

    @Test
    void updateCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
        when(customerRepository.save(customer)).thenReturn(customer);
        CustomerDto customerDtoResult = customerService.updateCustomer(1L, customerDto);
        assertEquals(customerDto.getEmail(), customerDtoResult.getEmail());
    }

    @Test
    void deleteCustomer() {
        String expected = "Customer was successfully deleted!";
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        String result = customerService.deleteCustomer(1L);
        verify(customerRepository, times(1)).delete(customer);
        assertEquals(expected, result);
    }

    @Test
    void testGetCustomerByIdHappyFlow() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer customerResult = customerService.getCustomerById(1L);
        assertEquals(customer.getLastName(), customerResult.getLastName());
        assertEquals(customer.getEmail(), customerResult.getEmail());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerByIdNegativeFlow() {
        String expected = "Customer not found with id: 1";
        when(customerRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Customer", "id", 1L));
        try {
            customerService.getCustomerById(1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void createBooking() {
        when(modelMapper.map(bookingDto, Booking.class)).thenReturn(booking);
        when(scheduleService.getScheduleByIdAsSchedule(1L)).thenReturn(schedule);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(modelMapper.map(booking, BookingDto.class)).thenReturn(bookingDto);
        BookingDto bookingDtoResult = customerService.createBooking(bookingDto, 1L, 1L);
        verify(customerRepository, times(1)).findById(1L);
        verify(scheduleService, times(1)).getScheduleByIdAsSchedule(1L);
        assertEquals(bookingDto.getCompartment(), bookingDtoResult.getCompartment());
    }

    @Test
    void updateBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(modelMapper.map(booking, BookingDto.class)).thenReturn(bookingDto);
        BookingDto bookingDtoResult = customerService.updateBooking(1L, bookingDto);
        assertEquals(bookingDto.getSeat(), bookingDtoResult.getSeat());
        assertEquals(bookingDto.getCompartment(), bookingDtoResult.getCompartment());
        assertEquals(bookingDto.getTrainClass(), bookingDtoResult.getTrainClass());
    }

    @Test
    void deleteBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        String result = customerService.deleteBooking(1L);
        verify(bookingRepository, times(1)).delete(booking);
        assertEquals("Booking was deleted successfully!", result);
    }

    @Test
    void getBookingByIdHappyFlow() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Booking bookingResult = customerService.getBookingById(1L);
        assertEquals(booking.getSeat(), bookingResult.getSeat());
        assertEquals(booking.getSchedule(), bookingResult.getSchedule());
    }

    @Test
    void getBookingByIdNegativeFlow() {
        String expected = "Booking not found with id: 1";
        when(bookingRepository.findById(1L)).thenThrow(new ResourceNotFoundException("Booking", "id", 1L));
        try{
            customerService.getBookingById(1L);
        }catch (ResourceNotFoundException e){
            assertEquals(expected, e.getMessage());
        }
    }

    @Test
    void saveAndMapToDto() {
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(modelMapper.map(booking, BookingDto.class)).thenReturn(bookingDto);
        BookingDto bookingDtoResult = customerService.saveAndMapToDto(booking);
        assertEquals(bookingDto.getTrainClass(), bookingDtoResult.getTrainClass());
        verify(bookingRepository, times(1)).save(booking);
    }
}