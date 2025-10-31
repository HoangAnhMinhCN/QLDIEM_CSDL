package com.qldiem.demo.WebApiController;

import com.qldiem.demo.DataMapping.CourseJoined;
import com.qldiem.demo.DataMapping.Student;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.StudentRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Api
@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public CourseRepository courseRepository;
    
    @GetMapping("/info/{student_id}")
    public ResponseEntity<List<Student>> getStudentInfo(@PathVariable("student_id") String studentId) {
        List<Student> result = studentRepository.show_student_info(studentId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
}
