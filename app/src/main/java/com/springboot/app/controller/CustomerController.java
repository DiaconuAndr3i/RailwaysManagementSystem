package com.springboot.app.controller;

import com.springboot.app.payload.CustomerDto;
import com.springboot.app.payload.CustomerWithBookingsDto;
import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.service.interfaces.CustomerService;
import com.springboot.app.utils.SortPageConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> insertCustomer(@RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.insertCustomer(customerDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<PagedSortedDto> getAllCustomers(
        @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
        @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
        @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(customerService.getAllCustomersGeneric(pageNo, pageSize, sortBy, sortDir, new CustomerDto()), HttpStatus.OK);
    }

    @GetMapping("/booking")
    public ResponseEntity<PagedSortedDto> getAllCustomersWithBookings(
            @RequestParam(value = "pageNo", defaultValue = SortPageConst.PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = SortPageConst.PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = SortPageConst.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = SortPageConst.SORT_DIRECTION, required = false) String sortDir
    ){
        return new ResponseEntity<>(customerService.getAllCustomersGeneric(pageNo, pageSize, sortBy, sortDir, new CustomerWithBookingsDto()), HttpStatus.OK);
    }

    @GetMapping("/{idCustomer}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable(name = "idCustomer") Long id){
        return new ResponseEntity<>(customerService.getCustomerById(id, new CustomerDto()), HttpStatus.OK);
    }

    @GetMapping("/{idCustomer}/booking")
    public ResponseEntity<CustomerDto> getCustomerBookingsById(@PathVariable(name = "idCustomer") Long id){
        return new ResponseEntity<>(customerService.getCustomerById(id, new CustomerWithBookingsDto()), HttpStatus.OK);
    }

    @PutMapping("/{idCustomer}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable(name = "idCustomer") Long id,
                                                      @RequestBody CustomerDto customerDto){
        return new ResponseEntity<>(customerService.updateCustomer(id, customerDto), HttpStatus.OK);
    }

    @DeleteMapping("/{idCustomer}")
    public ResponseEntity<String> deleteCustomer(@PathVariable(name = "idCustomer") Long id){
        return new ResponseEntity<>(customerService.deleteCustomer(id), HttpStatus.OK);
    }
}
