package com.qldiem.demo.Repository;

import com.qldiem.demo.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.*;

public interface TeacherRepository  extends JpaRepository<Teacher,String>{
    Optional<Teacher> findByUserName(String userName);

    Boolean existsByUserName(String userName);
}
