package com.taskflow.api.mapper;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.collaborator.CollaboratorUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CollaboratorMapper {

    @Mapping(target = "user.email", source = "userEmail")
    @Mapping(target = "user.password", source = "userPassword")
    @Mapping(target = "user.role", source = "userRole")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "manager", source = "manager")
    void updateCollaboratorFromDto(CollaboratorUpdateRequest request, @MappingTarget Collaborator collaborator);
}
