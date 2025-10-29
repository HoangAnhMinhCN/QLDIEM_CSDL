package com.qldiem.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;
import com.qldiem.demo.Relations.CourseJoined;

import java.util.*;

public interface TeacherRepository  extends JpaRepository<Table,String>{
    //Tra ve tu database cac course ma teacher dang day
    @Query(value = "CALL show_course_teached(:teacher_id_param)", nativeQuery = true)
    List<CourseJoined> show_course_teached(@Param("student_id_param") String student_id);

    //Tao mot course moi
    @Modifying //without @Modifying, then when your app runs, Spring tries to treat it as a SELECT query.
    @Transactional //without @Transactional, no automatic rollback if error happen
    @Query(value = "CALL create_course(:course_name_param, :teacher_id_param, :start_date_param)", nativeQuery = true)
    void create_course(@Param("course_name_param") String course_name,@Param("teacher_id_param") String teacher_id,@Param("start_date_param") String start_date);

    //Xoa di mot course
    @Modifying //without @Modifying, then when your app runs, Spring tries to treat it as a SELECT query.
    @Transactional //without @Transactional, no automatic rollback if error happen
    @Query(value = "CALL delete_course(:course_id_param)", nativeQuery = true)
    void delete_course(@Param("course_id_param") String course_id);
}
