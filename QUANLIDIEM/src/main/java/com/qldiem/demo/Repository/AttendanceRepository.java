package com.qldiem.demo.Repository;

import com.qldiem.demo.Entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

    Optional<Attendance> findByStudentIdAndCourseId(String studentId, String courseId);

    List<Attendance> findByStudentId(String studentId);
    List<Attendance> findByCourseId(String courseId);

    long countByCourseId(String courseId);
}
