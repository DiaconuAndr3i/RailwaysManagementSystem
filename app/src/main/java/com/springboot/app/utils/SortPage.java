package com.springboot.app.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SortPage {
    private static Sort getSorter(String sortBy, String sortDir){
        return sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    }

    public static Pageable getPageable(int pageNo, int pageSize, String sortBy, String sortDir){
        return PageRequest.of(pageNo, pageSize, getSorter(sortBy, sortDir));
    }

}
