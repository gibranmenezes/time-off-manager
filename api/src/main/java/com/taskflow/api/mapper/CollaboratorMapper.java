package com.taskflow.api.mapper;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.collaborator.CollaboratorCreationResponse;
import com.taskflow.api.domain.collaborator.CollaboratorDetails;
import com.taskflow.api.domain.collaborator.CollaboratorUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CollaboratorMapper {

    @Mapping(target = "managerName", expression = "java(collaborator.getManager() != null ? collaborator.getManager().getName() : null)")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "username", source = "user.username")
    CollaboratorDetails toDetails(Collaborator collaborator);
}
