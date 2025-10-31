package com.qldiem.demo.Repository;
import com.qldiem.demo.DataMapping.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<DummyEntity,String>{
    //Tra ve tu database thong tin cua student
    @Query(value="CALL show_student_info(:student_id_param)",nativeQuery = true)
    List<Student> show_student_info(@Param("student_id_param") String student_id);

    //Thay doi thong tin cua student
    @Modifying
    @Transactional
    @Query(value="CALL update_student(:student_id_param, :student_name_param, :user_name_param, :user_password_param, :birthday_param, :gender_param)",nativeQuery = true)
    void update_student(
        @Param("student_id_param") String student_id, 
        @Param("student_name_param") String student_name, 
        @Param("user_name_param") String user_name, 
        @Param("user_password_param") String user_password, 
        @Param("birthday_param") String birthday, 
        @Param("gender_param") String gender
    );

    //Tra ve tu database cac course ma student dang hoc
    @Query(value = "CALL show_course_joined(:student_id_param)", nativeQuery = true)
    List<CourseJoined> show_course_joined(@Param("student_id_param") String student_id);  

    //Tham gia course
    @Modifying
    @Transactional
    @Query(value= "CALL join_course(:student_id_param, :course_id_param, :join_date_param)", nativeQuery=true)
    void join_course(@Param("student_id_param") String student_id, @Param("course_id_param") String course_id, @Param("join_date_param") String join_date);   

    //Roi khoi course
    @Modifying
    @Transactional
    @Query(value= "CALL leave_course(:student_id_param, :course_id_param)", nativeQuery=true)
    void leave_course(@Param("student_id_param") String student_id, @Param("course_id_param") String course_id);  

    

    
  
}
