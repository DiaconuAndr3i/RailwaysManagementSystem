package com.springboot.app.service.interfaces;

import com.springboot.app.payload.PagedSortedDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ObjectsMapperService {
    PagedSortedDto mapToSortedPaged(List<?> DtoList, Page<?> pages);
    <S,D> Object mapFromTo(S source, D destination);
}
