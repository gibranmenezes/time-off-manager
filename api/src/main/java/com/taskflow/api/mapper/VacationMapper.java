package com.taskflow.api.mapper;


import com.taskflow.api.domain.vacation.Vacation;
import com.taskflow.api.domain.vacation.VacationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VacationMapper {

    @Mapping(target = "collaboratorName", source = "collaborator.name")
    @Mapping(target = "collaboratorId", source = "collaborator.id")
    @Mapping(target = "status", source = "vacationStatus")
    VacationResponse toResponse(Vacation vacation);
}
