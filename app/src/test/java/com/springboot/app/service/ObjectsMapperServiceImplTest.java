package com.springboot.app.service;

import com.springboot.app.payload.PagedSortedDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ObjectsMapperServiceImplTest {
    @InjectMocks
    private ObjectsMapperServiceImpl objectsMapperService;

    @Test
    void mapToSortedPaged() {
        Object obj = new Object();
        List<Object> listDto = new ArrayList<>(List.of(obj));
        Page<Object> pages = new PageImpl<>(new ArrayList<>(List.of(obj)));
        PagedSortedDto pagedSortedDto = new PagedSortedDto(pages.getNumber(), pages.getSize(), pages.getTotalElements(),
                pages.getTotalPages(), pages.isFirst(), pages.isLast(), listDto);
        PagedSortedDto pagedSortedDtoResult = objectsMapperService.mapToSortedPaged(listDto, pages);
        assertEquals(pagedSortedDto.getPageSize(), pagedSortedDtoResult.getPageSize());
        assertEquals(pagedSortedDto.getTotalElements(), pagedSortedDtoResult.getTotalElements());
    }
}