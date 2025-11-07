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
    @Modifying
    @Transactional
    @Query(value = "CALL create_course(:course_name_param, :teacher_id_param, :start_date_param)", nativeQuery = true)
    void create_course(
            @Param("course_name_param") String course_name,
            @Param("teacher_id_param") String teacher_id,
            @Param("start_date_param") String start_date
    );

    //Cap nhat thong tin cua course
    @Modifying
    @Transactional
    @Query(value="CALL update_course(:course_id_param, :course_name_param, :start_date_param, :teacher_id_param)", nativeQuery = true)
    void update_course(
            @Param("course_id_param") String course_id,
            @Param("course_name_param") String course_name,
            @Param("start_date_param") String start_date,
            @Param("teacher_id_param") String teacher_id
    );

    //Xoa di mot course
    @Modifying
    @Transactional
    @Query(value = "CALL delete_course(:course_id_param, :teacher_id_param)", nativeQuery = true)
    void delete_course(
            @Param("course_id_param") String course_id,
            @Param("teacher_id_param") String teacher_id
    );

    //Tao mot bai thi
    @Modifying
    @Transactional
    @Query(value = "CALL create_exam(:exam_name_param, :course_id_param, :exam_date_param, :created_date_param)", nativeQuery = true)
    void create_exam(
            @Param("exam_name_param") String exam_name,
            @Param("course_id_param") String course_id,
            @Param("exam_date_param") String exam_date,
            @Param("created_date_param") String created_date
    );

    //Cap nhat thong tin bai thi
    @Modifying
    @Transactional
    @Query(value="CALL update_exam(:exam_id_param, :exam_name_param, :exam_date_param, :teacher_id_param)",nativeQuery = true)
    void update_exam(
            @Param("exam_id_param") String exam_id,
            @Param("exam_name_param") String exam_name,
            @Param("exam_date_param") String exam_date,
            @Param("teacher_id_param") String teacher_id
    );

    //Xoa mot bai thi
    @Modifying
    @Transactional
    @Query(value = "CALL delete_exam(:exam_id_param, :teacher_id_param)",nativeQuery = true)
    void delete_exam(
            @Param("exam_id_param") String exam_id,
            @Param("teacher_id_param") String teacher_id
    );

    //thêm student
    @Modifying
    @Transactional
    @Query(value = "CALL add_student_to_course(:student_id, :course_id, :teacher_id)", nativeQuery = true)
    void add_student_to_course(
            @Param("student_id") String student_id,
            @Param("course_id") String course_id,
            @Param("teacher_id") String teacher_id
    );

    //xóa student
    @Modifying
    @Transactional
    @Query(value = "CALL remove_student_from_course(:student_id, :course_id, :teacher_id)", nativeQuery = true)
    void  remove_student_from_course(
            @Param("student_id") String student_id,
            @Param("course_id") String course_id,
            @Param("teacher_id") String teacher_id
    );

        //Cap nhat diem
        @Modifying
        @Transactional
        @Query(value = "CALL update_score(:exam_id_param, :student_id_param, :score_param, :teacher_id_param)",nativeQuery = true)
        void update_score(@Param("exam_id_param") String exam_id,
                                          @Param("student_id_param") String student_id,
                                          @Param("score_param") String score,
                                          @Param("teacher_id_param") String teacher_id);

    //Ktra username login
    @Query(value = "SELECT COUNT(*) FROM teacher WHERE user_name = :username_param", nativeQuery = true)
    Integer check_username_exist(@Param("username_param") String user_name_param);

    @Query(value = "CALL find_teacher_by_username(:username_param)", nativeQuery = true)
    Optional<Teacher> find_teacher_by_username(@Param("username_param") String user_name_param);

    @Modifying
    @Transactional
    @Query(value = "CALL create_teacher(:teacher_id, :teacher_name, :user_name, :user_password, :birthday, :gender)", nativeQuery = true)
    void create_teacher(
            @Param("teacher_id") String teacher_id,
            @Param("teacher_name") String teacher_name,
            @Param("user_name") String user_name,
            @Param("user_password") String user_password,
            @Param("birthday") String birthday,
            @Param("gender") String gender
    );
}
