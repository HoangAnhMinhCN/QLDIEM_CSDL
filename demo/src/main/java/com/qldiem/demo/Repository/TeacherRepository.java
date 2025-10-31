package com.qldiem.demo.Repository;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qldiem.demo.DataMapping.*;


import jakarta.transaction.Transactional;

@Repository
public interface TeacherRepository extends JpaRepository<DummyEntity,String>{
    //Tra ve tu database thong tin cua teacher
    @Query(value="CALL show_teacher_info(:teacher_id_param)",nativeQuery = true)
    List<Teacher> show_teacher_info(@Param("teacher_id_param") String teacher_id);

    //Thay doi thong tin cua teacher
    @Modifying
    @Transactional
    @Query(value="CALL update_teacher(:teacher_id_param, :teacher_name_param, :user_name_param, :user_password_param, :birthday_param, :gender_param)",nativeQuery = true)
    void update_teacher(
        @Param("teacher_id_param") String teacher_id, 
        @Param("teacher_name_param") String teacher_name, 
        @Param("user_name_param") String user_name, 
        @Param("user_password_param") String user_password, 
        @Param("birthday_param") String birthday, 
        @Param("gender_param") String gender
    );

    //Tra ve tu database cac course ma teacher dang day
    @Query(value = "CALL show_course_teached(:teacher_id_param)", nativeQuery = true)
    List<CourseTeached> show_course_teached(@Param("teacher_id_param") String teacher_id);

    //Tao mot course moi
    @Modifying //without @Modifying, then when your app runs, Spring tries to treat it as a SELECT query.
    @Transactional //without @Transactional, no automatic rollback if error happen
    @Query(value = "CALL create_course(:course_name_param, :teacher_id_param, :start_date_param)", nativeQuery = true)
    void create_course(@Param("course_name_param") String course_name,@Param("teacher_id_param") String teacher_id,@Param("start_date_param") String start_date);
    
    //Cap nhat thong tin cua course
    @Modifying
    @Transactional 
    @Query(value="CALL update_course(:course_id_param, :course_name_param)",nativeQuery = true)
    void update_course(@Param("course_id_param") String course_id, @Param("course_name_param") String course_name);

    //Xoa di mot course
    @Modifying //without @Modifying, then when your app runs, Spring tries to treat it as a SELECT query.
    @Transactional //without @Transactional, no automatic rollback if error happen
    @Query(value = "CALL delete_course(:course_id_param)", nativeQuery = true)
    void delete_course(@Param("course_id_param") String course_id);

    //Tao mot bai thi
    @Modifying
    @Transactional 
    @Query(value = "CALL create_exam(:exam_name_param, :teacher_id_param, :course_id_param, :exam_date_param, :created_date_param)", nativeQuery = true)
    void create_exam(
        @Param("exam_name_param") String exam_name, 
        @Param("teacher_id_param") String teacher_id, 
        @Param("course_id_param") String course_id, 
        @Param("exam_date_param") String exam_date, 
        @Param("created_date_param") String created_date
    );

    //Cap nhat thong tin bai thi
    @Modifying
    @Transactional
    @Query(value="CALL update_exam(:exam_id_param, :exam_name_param, :exam_date_param )",nativeQuery = true)
    void update_exam(@Param("exam_id_param") String exam_id, @Param("exam_name_param") String exam_name, @Param("exam_date_param") String exam_date);

    //Xoa mot bai thi
    @Modifying
    @Transactional
    @Query(value = "CALL delete_exam(:exam_id_param)",nativeQuery = true)
    void delete_exam( @Param("exam_id_param") String exam_id);

    //Cap nhat diem 
    @Modifying
    @Transactional
    @Query(value = "CALL update_score(:exam_id_param, :student_id_param, :score_param )",nativeQuery = true)
    void update_score(@Param("exam_id_param") String exam_id, @Param("student_id_param") String student_id,@Param("score_param") String score);
}
