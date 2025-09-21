package com.taskflow.api.respository;

import com.taskflow.api.domain.enums.VacationStatus;
import com.taskflow.api.domain.vacation.Vacation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface VacationRepository extends JpaRepository<Vacation, Long> {



    @Query("""
        SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END\s
       FROM Vacation v
       WHERE (v.startDate <= :endDate AND v.endDate >= :startDate)\s
       AND (
refac           (v.vacationStatus = 'APPROVED' AND (:managerId is null or v.collaborator.manager.id = :managerId))
           OR\s
           (v.vacationStatus = 'PENDING' AND v.collaborator.id = :collaboratorId)
       )
    """)
    boolean existsOverlappingRequests(
            @Param("managerId") Long managerId,
            @Param("collaboratorId") Long collaboratorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select v
        from Vacation v
        where (:managerId is null or v.collaborator.manager.id = :managerId)
          and (:collaboratorId is null or v.collaborator.id = :collaboratorId)
          and (:vacationId is null or v.id = :vacationId)
          and (:collaboratorName is null or lower(v.collaborator.name) like lower(concat('%', :collaboratorName, '%')))
          and (:status is null or v.vacationStatus = :status)
          and (:fromDate is null or v.endDate >= :fromDate)
          and (:toDate is null or v.startDate <= :toDate)
          and (:status is null or v.vacationStatus = :status)
        """)
    Page<Vacation> findAllByScopeAndFilters(
            @Param("managerId") Long managerId,
            @Param("collaboratorId") Long collaboratorId,
            @Param("vacationId") Long vacationId,
            @Param("collaboratorName") String collaboratorName,
            @Param("status") VacationStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );


}
