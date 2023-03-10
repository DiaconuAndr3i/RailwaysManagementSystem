package com.springboot.app.service;

import com.springboot.app.payload.PagedSortedDto;
import com.springboot.app.service.interfaces.ObjectsMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectsMapperServiceImpl implements ObjectsMapperService {
    @Override
    public PagedSortedDto mapToSortedPaged(List<?> DtoList, Page<?> pages){
        return PagedSortedDto.builder()
                .list((List<Object>) DtoList)
                .pageNo(pages.getNumber())
                .pageSize(pages.getSize())
                .totalElements(pages.getTotalElements())
                .totalPages(pages.getTotalPages())
                .first(pages.isFirst())
                .last(pages.isLast())
                .build();
    }
}
