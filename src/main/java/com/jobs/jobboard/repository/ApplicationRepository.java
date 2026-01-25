package com.jobs.jobboard.repository;

import com.jobs.jobboard.entity.Application;
import com.jobs.jobboard.entity.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a WHERE a.id = :id AND a.deletedAt IS NULL")
    Optional<Application> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT a FROM Application a WHERE a.deletedAt IS NULL")
    List<Application> findAllNotDeleted();

    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidateId AND a.jobVacancy.id = :jobId AND a.deletedAt IS NULL")
    Optional<Application> findByCandidateIdAndJobIdAndNotDeleted(
            @Param("candidateId") Long candidateId,
            @Param("jobId") Long jobId
    );

    @Query("SELECT a FROM Application a WHERE a.candidate.id = :candidateId AND a.deletedAt IS NULL")
    List<Application> findByCandidateIdAndNotDeleted(@Param("candidateId") Long candidateId);

    @Query("SELECT a FROM Application a WHERE a.jobVacancy.id = :jobId AND a.deletedAt IS NULL")
    List<Application> findByJobIdAndNotDeleted(@Param("jobId") Long jobId);

    @Query("SELECT a FROM Application a WHERE a.jobVacancy.company.id = :companyId AND a.deletedAt IS NULL")
    List<Application> findByCompanyIdAndNotDeleted(@Param("companyId") Long companyId);

    @Query("""
        SELECT a FROM Application a 
        WHERE a.jobVacancy.id = :jobId 
        AND a.deletedAt IS NULL 
        AND (:status IS NULL OR a.status = :status)
    """)
    Page<Application> findByJobIdAndStatusAndNotDeleted(
            @Param("jobId") Long jobId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );
}