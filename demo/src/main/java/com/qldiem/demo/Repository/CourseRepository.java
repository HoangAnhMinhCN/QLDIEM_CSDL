package com.qldiem.demo.Repository;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qldiem.demo.DataMapping.StudentExamsScore;
import com.qldiem.demo.DataMapping.StudentList;

public interface CourseRepository extends JpaRepository<DummyEntity,String> {
    //Hien danh sach student trong course
    @Query(value="CALL show_course_studentList(:course_id_param)",nativeQuery = true)
    List<StudentList> show_course_StudentLists(@Param("course_id_param") String course_id);

    //Tra ve tu database diem cac bai thi cua 1 student trong 1 course
    @Query(value = "CALL show_student_course_exams_score(:student_id_param, :course_id_param)", nativeQuery=true)
    List<StudentExamsScore> show_student_exams_score(@Param("student_id_param") String student_id, @Param("course_id_param") String course_id);


}
