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
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Vacation r
        WHERE r.vacationStatus = 'APPROVED'
          AND (:startDate BETWEEN r.startDate AND r.endDate
            OR :endDate BETWEEN r.startDate AND r.endDate
            OR r.startDate BETWEEN :startDate AND :endDate)
    """)
    boolean existsOverlappingRequests(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
        select v
        from Vacation v
        where (:managerId is null or v.collaborator.manager.id = :managerId)
          and (:collaboratorId is null or v.collaborator.id = :collaboratorId)
          and (:status is null or v.vacationStatus = :status)
          and (:fromDate is null or v.endDate >= :fromDate)
          and (:toDate is null or v.startDate <= :toDate)
        """)
    Page<Vacation> findAllByScopeAndFilters(
            @Param("managerId") Long managerId,
            @Param("collaboratorId") Long collaboratorId,
            @Param("status") VacationStatus status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );


}
