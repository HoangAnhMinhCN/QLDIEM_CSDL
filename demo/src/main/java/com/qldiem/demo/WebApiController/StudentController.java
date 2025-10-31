package com.qldiem.demo.WebApiController;

import com.qldiem.demo.DataMapping.CourseJoined;
import com.qldiem.demo.DataMapping.Student;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.StudentRepository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.transaction.Transactional;
//Api
@RestController
@RequestMapping("/api/student/{student_id}")
public class StudentController {
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public CourseRepository courseRepository;
    
    //Phan thong tin hoc sinh
    @GetMapping("/info")
    public ResponseEntity<List<Student>> getStudentInfo(@PathVariable("student_id") String studentId) {
        List<Student> result = studentRepository.show_student_info(studentId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    //Phan cap nhat thong tin hoc sinh
    @PostMapping("/update")
    public ResponseEntity<String> updateStudent(@RequestBody Map<String, Object> requestData) {
        try {
            //Lay du lieu tu json chuyen den
            String studentId = (String) requestData.get("student_id");
            String studentName = (String) requestData.get("student_name");
            String userName = (String) requestData.get("user_name");
            String userPassword = (String) requestData.get("user_password");
            String birthday = (String) requestData.get("birthday");
            String gender = (String) requestData.get("gender");

            studentRepository.update_student(studentId, studentName, userName, userPassword, birthday, gender);

            return ResponseEntity.ok("Student information updated");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating student: " + e.getMessage());
        }
    }    

    //Phan hien cac lop da tham gia
    @GetMapping("/courses")
    public ResponseEntity<List<CourseJoined>> getStudentCourses(@PathVariable("student_id") String studentId) {
        try {
            List<CourseJoined> courses = studentRepository.show_course_joined(studentId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    //Phan tham gia lop bang course_id
    @PostMapping("/join/{course_id}")
    @Transactional
    public ResponseEntity<String> joinCourse(@PathVariable("student_id") String studentId, @PathVariable("course_id") String courseId) {
        try {
            String joinDate = LocalDate.now().toString();
            studentRepository.join_course(studentId, courseId, joinDate);
            return ResponseEntity.ok("Student joined course");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to join course: " + e.getMessage());
        }
    }
}
