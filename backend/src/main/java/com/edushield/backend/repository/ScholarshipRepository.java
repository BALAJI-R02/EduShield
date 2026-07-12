package com.edushield.backend.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edushield.backend.entity.Category;
import com.edushield.backend.entity.Scholarship;

public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {

    @Query("SELECT s FROM Scholarship s WHERE " +
           "(s.minIncome IS NULL OR :income >= s.minIncome) AND " +
           "(s.maxIncome IS NULL OR :income <= s.maxIncome) AND " +
           "(s.category = com.edushield.backend.entity.Category.ANY OR s.category = :category) AND " +
           "(s.minMarks IS NULL OR :marksAvg >= s.minMarks) AND " +
           "(s.state IS NULL OR s.state = :state) AND " +
           "s.deadline >= :today")
    List<Scholarship> findMatchingScholarships(
    @Param("income") Double income,
    @Param("category") Category category,
    @Param("marksAvg") Double marksAvg,
    @Param("state") String state,
    @Param("today") LocalDate today
);
}