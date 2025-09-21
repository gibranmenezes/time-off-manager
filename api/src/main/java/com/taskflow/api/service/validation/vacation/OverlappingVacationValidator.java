package com.taskflow.api.service.validation.vacation;

import com.taskflow.api.domain.collaborator.Collaborator;
import com.taskflow.api.domain.exception.ValidationErrorType;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.vacation.Vacation;
import com.taskflow.api.respository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OverlappingVacationValidator implements VacationRequestValidation, VacationUpdateValidation {

    private final VacationRepository vacationRepository;

    @Override
    public void validate(Collaborator collaborator, LocalDate startDate, LocalDate endDate) {
        if (vacationRepository.existsOverlappingRequests(collaborator.getManager().getId(), collaborator.getId(), startDate, endDate)) {
            throw new ValidationException(
                "There is an approved vacation in this period",
                ValidationErrorType.BUSINESS_RULE_VIOLATION
            );
        }
    }

    @Override
    public void validateUpdate(Vacation vacation) {
        validate(vacation.getCollaborator(), vacation.getStartDate(), vacation.getEndDate());

    }
}