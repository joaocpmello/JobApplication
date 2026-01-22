package com.jobs.jobboard.repository;

import com.jobs.jobboard.entity.JobStatus;
import com.jobs.jobboard.entity.JobVacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobVacancy, Long> {

    @Query("SELECT j FROM JobVacancy j WHERE j.id = :id AND j.deletedAt IS NULL")
    Optional<JobVacancy> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT j FROM JobVacancy j WHERE j.deletedAt IS NULL")
    List<JobVacancy> findAllNotDeleted();

    @Query("SELECT j FROM JobVacancy j WHERE j.status = :status AND j.deletedAt IS NULL")
    List<JobVacancy> findByStatusAndNotDeleted(@Param("status") JobStatus status);

    @Query("SELECT j FROM JobVacancy j WHERE j.company.id = :companyId AND j.deletedAt IS NULL")
    List<JobVacancy> findByCompanyIdAndNotDeleted(@Param("companyId") Long companyId);

    @Query("SELECT j FROM JobVacancy j WHERE j.company.user.id = :userId AND j.id = :jobId AND j.deletedAt IS NULL")
    Optional<JobVacancy> findByIdAndCompanyUserIdAndNotDeleted(@Param("jobId") Long jobId, @Param("userId") Long userId);
}
