package com.qldiem.demo.Repository;

import com.qldiem.demo.Relations.CourseJoined;
import com.qldiem.demo.Entity.LopHoc; // Dùng Entity giả
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseStoredProcRepository extends JpaRepository<LopHoc, String> {

    // Tra ve tu database cac course ma student dang hoc
    @Query(value = "CALL show_course_joined(:student_id_param)", nativeQuery = true)
    List<CourseJoined> show_course_joined(@Param("student_id_param") String student_id);

}
