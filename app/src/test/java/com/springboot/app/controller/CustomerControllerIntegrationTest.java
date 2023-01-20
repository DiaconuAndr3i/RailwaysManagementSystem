package com.springboot.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.app.config.AbstractContainer;
import com.springboot.app.payload.booking.BookingDto;
import com.springboot.app.payload.customer.CustomerDto;
import com.springboot.app.utils.enums.Class;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import com.springboot.app.utils.enums.Type;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest extends AbstractContainer {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenAnInstantiationOfDBThroughBeanContext_whenGetCustomerById_thenCustomerDto() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/customer/{idCustomer}", 1L));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email1@email1.com"));
    }

    @Test
    public void givenCustomer_whenInsertCustomer_thenCustomerDto() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setLastName("Diaconu");
        customerDto.setFirstName("Andrei");
        customerDto.setEmail("diaconu.andrei99@gmail.com");

        ResultActions resultActions =  mockMvc
                .perform(MockMvcRequestBuilders.post("/api/customer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDto.getFirstName()));
    }

    @Test
    public void givenAnInstantiationOfDBThroughBeanContext_whenGetAllCustomers_thenPagedSortedDto() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/customer"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1));
    }

    @Test
    public void givenAnInstantiationOfDBThroughBeanContext_whenGetAllCustomersWithBookings_thenPagedSortedDto() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/customer/booking"));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.first").value(Boolean.TRUE.toString()));
    }

    @Test
    public void givenAnInstantiationOfDBThroughBeanContext_whenGetCustomerBookingsById_thenPagedSortedDto() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/customer/{idCustomer}/booking", 1L));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.bookings.size()").value(2));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("last_name_1"));
    }

    @Test
    public void givenCustomer_whenUpdateCustomer_thenCustomerDto() throws Exception {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setLastName("Diaconu1");
        customerDto.setFirstName("Andrei1");
        customerDto.setEmail("diaconu1.andrei99@gmail.com");
        ResultActions resultActionsBeforeUpdate = mockMvc
                .perform(MockMvcRequestBuilders.get("/api/customer/{idCustomer}", 1L));
        resultActionsBeforeUpdate.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("first_name_1"));
        resultActionsBeforeUpdate.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("last_name_1"));
        resultActionsBeforeUpdate.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email1@email1.com"));
        ResultActions resultActionsAfterUpdate = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/customer/{idCustomer}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(customerDto)));
        resultActionsAfterUpdate.andExpect(MockMvcResultMatchers.status().isOk());
        resultActionsAfterUpdate.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(customerDto.getLastName()));
        resultActionsAfterUpdate.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(customerDto.getFirstName()));
        resultActionsAfterUpdate.andExpect(MockMvcResultMatchers.jsonPath("$.email").value(customerDto.getEmail()));
    }

    @Test
    public void givenAnInstantiationOfDBThroughBeanContext_whenDeleteCustomer_thenDeletedSuccessfully() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/customer/{idCustomer}", 1L));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string("Customer was successfully deleted!"));
    }

    @Test
    public void givenBooking_whenCreateBooking_thenBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setTrainClass(Class.CLASS_I);
        bookingDto.setCompartment("C2");
        bookingDto.setSeat(114);
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/customer/{customerId}/schedule/{scheduleId}", 3L, 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.schedule.train.trainType").value(Type.INTER_CITY.toString()));
    }

    @Test
    public void givenBooking_whenUpdateBooking_thenBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setTrainClass(Class.CLASS_II);
        bookingDto.setCompartment("C2");
        bookingDto.setSeat(113);
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/customer/booking/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.trainClass").value(bookingDto.getTrainClass().toString()));
        resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.seat").value(bookingDto.getSeat()));
    }

    @Test
    public void givenBooking_whenUpdateBooking_thenError() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setTrainClass(Class.CLASS_II);
        bookingDto.setCompartment("C2");
        bookingDto.setSeat(113);
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.put("/api/customer/booking/{id}", 6L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)));
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void givenAnInstantiationOfDBThroughBeanContext_whenDeleteBooking_thenDeletedSuccessfully() throws Exception {
        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.delete("/api/customer/booking/{id}", 1L));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        resultActions.andExpect(MockMvcResultMatchers.content().string("Booking was deleted successfully!"));
    }
}
