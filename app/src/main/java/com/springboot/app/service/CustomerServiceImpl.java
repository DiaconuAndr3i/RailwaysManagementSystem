package com.springboot.app.service;

import com.springboot.app.entity.Customer;
import com.springboot.app.payload.CustomerDto;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.repository.CustomerRepository;
import com.springboot.app.service.interfaces.CustomerService;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import com.springboot.app.utils.SortPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ObjectsMapperService objectsMapperService;
    @Override
    public CustomerDto insertCustomer(CustomerDto customerDto) {
        return (CustomerDto) objectsMapperService
                .mapFromTo(customerRepository
                                .save((Customer) objectsMapperService.mapFromTo(customerDto, new Customer())), new CustomerDto());
    }


    @Override
    public <T> PagedSortedDto getAllCustomersGeneric(int pageNo, int pageSize, String sortBy, String sortDir, T object) {
        Page<Customer> customerPages = customerRepository.findAll(SortPage.getPageable(pageNo, pageSize, sortBy, sortDir));
        List<CustomerDto> listObject = customerPages.getContent()
                .stream()
                .map(customer -> (CustomerDto) objectsMapperService.mapFromTo(customer, object))
                .toList();
        return objectsMapperService.mapToSortedPaged(listObject, customerPages);
    }

    @Override
    public <T> CustomerDto getCustomerById(Long idCustomer, T object) {
        Customer customer = getCustomerById(idCustomer);
        return (CustomerDto) objectsMapperService.mapFromTo(customer, object);
    }

    @Override
    public CustomerDto updateCustomer(Long idCustomer, CustomerDto customerDto) {
        Customer customer = getCustomerById(idCustomer);

        customer.setEmail(customerDto.getEmail());
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());

        return (CustomerDto) objectsMapperService
                .mapFromTo(customerRepository.save(customer), new CustomerDto());
    }

    @Override
    public String deleteCustomer(Long idCustomer) {
        customerRepository.delete(getCustomerById(idCustomer));
        return "Customer was successfully deleted!";
    }

    private Customer getCustomerById(Long idCustomer){
        return customerRepository.findById(idCustomer).orElseThrow(RuntimeException::new);
    }
}
