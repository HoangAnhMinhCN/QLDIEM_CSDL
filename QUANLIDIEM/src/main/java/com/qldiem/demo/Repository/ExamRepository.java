package com.qldiem.demo.Repository;

import com.qldiem.demo.DataMapping.ExamDetail;
import com.qldiem.demo.DataMapping.ExamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<DummyEntity, String> {

    // lấy ds exam theo course
    @Query(value = "CALL show_course_exams(:course_id_param)", nativeQuery = true)
    List<ExamResponse> show_course_exams(@Param("course_id_param") String courseId);

    // lấy thông tin exam
    @Query(value = "CALL get_exam_by_id(:exam_id_param)", nativeQuery = true)
    Optional<ExamDetail> get_exam_by_id(@Param("exam_id_param") String examId);

    // lấy ds exam của student trong course
    @Query(value = "CALL show_student_exams_in_course(:student_id_param, :course_id_param)", nativeQuery = true)
    List<ExamDetail> show_student_exams_in_course(
            @Param("student_id_param") String studentId,
            @Param("course_id_param") String courseId
    );
}
