package com.qldiem.demo.Repository;
import com.qldiem.demo.Relations.CourseJoined;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import java.util.*;

//Noi luu tru cac ham goi stored procedure trong database lien quan den course
@Repository
public interface StudentRepository extends JpaRepository<Table,String>{
    //Tra ve tu database cac course ma student dang hoc
    @Query(value = "CALL show_course_joined(:student_id_param)", nativeQuery = true)
    List<CourseJoined> show_course_joined(@Param("student_id_param") String student_id);

    

    

    

}
