package com.qldiem.demo.Repository;

import com.qldiem.demo.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,String>{
    // Tìm sinh viên bằng user_name
    Optional<Student> findByUserName(String userName);

    // Kiểm tra xem user_name có tồn tại không
    Boolean existsByUserName(String userName);
}
