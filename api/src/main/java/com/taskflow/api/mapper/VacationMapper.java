package com.taskflow.api.mapper;


import com.taskflow.api.domain.vacation.VacationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VacationMapper {

    VacationResponse toResponse(com.taskflow.api.domain.vacation.Vacation vacation);
}
