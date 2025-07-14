package com.example.classpath.domain.enrollmentperiod.repository;

import com.example.classpath.domain.enrollmentperiod.entity.EnrollmentPeriod;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentPeriodRepository extends JpaRepository<EnrollmentPeriod, Long> {

    Optional<EnrollmentPeriod> findFirstBy();

    boolean existsBy();
}