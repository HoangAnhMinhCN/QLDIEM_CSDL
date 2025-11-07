package com.qldiem.demo.Repository;
import java.util.*;

import com.qldiem.demo.DataMapping.CourseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.qldiem.demo.DataMapping.StudentList;

public interface CourseRepository extends JpaRepository<DummyEntity,String> {
    //Hien danh sach student trong course
    @Query(value="CALL show_course_studentList(:course_id_param)",nativeQuery = true)
    List<StudentList> show_course_StudentLists(@Param("course_id_param") String course_id);

    //Hiện all course
    @Query(value = "CALL get_all_course()", nativeQuery = true)
    List<CourseResponse> getAllCourse();

    //Hiện all course chưa tham gia
    @Query(value = "CALL find_available_courses_for_student(:student_id_param)", nativeQuery = true)
    List<CourseResponse> findAvailableCoursesForStudent(@Param("student_id_param") String studentId);

    //tìm course = name
    @Query(value = "CALL search_course(:keyword_param)", nativeQuery = true)
    List<CourseResponse> searchCourse(@Param("keyword_param") String keyword);

    //tìm = id
    @Query(value = "CALL get_course_by_id(:course_id_param)", nativeQuery = true)
    Optional<CourseResponse> findCourseById(@Param("course_id_param") String courseId);
}
