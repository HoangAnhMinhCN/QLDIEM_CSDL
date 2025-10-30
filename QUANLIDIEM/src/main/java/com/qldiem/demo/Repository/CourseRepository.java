package com.qldiem.demo.Repository;

import com.qldiem.demo.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    List<Course> findByTeacherId(String teacher);

    List<Course> findByCourseNameContainingIgnoreCase(String courseName);

    @Query(value = "SELECT c.* FROM course c " +
            "LEFT JOIN attendance a ON c.course_id = a.course_id " +
            "  AND a.student_id = :studentId " +
            "WHERE a.id IS NULL",
            nativeQuery = true)
    List<Course> findAvailableCoursesForStudent(@Param("studentId") String studentId);
}
