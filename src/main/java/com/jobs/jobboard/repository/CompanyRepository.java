package com.jobs.jobboard.repository;

import com.jobs.jobboard.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Company> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT c FROM Company c WHERE c.user.id = :userId AND c.deletedAt IS NULL")
    Optional<Company> findByUserIdAndNotDeleted(@Param("userId") Long userId);

    @Query("SELECT c FROM Company c WHERE c.deletedAt IS NULL")
    java.util.List<Company> findAllNotDeleted();
}
