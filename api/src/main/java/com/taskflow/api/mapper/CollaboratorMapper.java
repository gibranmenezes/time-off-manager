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

    @Mapping(target = "user.email", source = "userEmail")
    @Mapping(target = "user.password", source = "userPassword")
    @Mapping(target = "user.role", source = "userRole")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "manager.id", source = "managerId")
    void updatedCollaboratorFromDto(CollaboratorUpdateRequest request, @MappingTarget Collaborator collaborator);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "managerId", source = "manager.id")
    CollaboratorCreationResponse toCreationResponse(Collaborator collaborator);

    @Mapping(target = "managerName", expression = "java(collaborator.getManager() != null ? collaborator.getManager().getName() : null)")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "email", source = "user.email")
    CollaboratorDetails toDetails(Collaborator collaborator);
}
