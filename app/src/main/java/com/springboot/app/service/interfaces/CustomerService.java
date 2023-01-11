package com.springboot.app.service.interfaces;

import com.springboot.app.payload.customer.CustomerDto;
import com.springboot.app.payload.PagedSortedDto;

public interface CustomerService {
    CustomerDto insertCustomer(CustomerDto customerDto);
    <T> PagedSortedDto getAllCustomersGeneric(int pageNo, int pageSize, String sortBy, String sortDir, T object);
    <T> CustomerDto getCustomerById(Long idCustomer, T object);
    CustomerDto updateCustomer(Long idCustomer, CustomerDto customerDto);
    String deleteCustomer(Long idCustomer);
}
