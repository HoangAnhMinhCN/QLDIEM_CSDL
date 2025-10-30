package com.qldiem.demo.Controller;


import com.qldiem.demo.Relations.CourseJoined;
import com.qldiem.demo.Entity.Student;
import com.qldiem.demo.Repository.CourseStoredProcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private CourseStoredProcRepository courseRepo;

    @GetMapping("/me/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseJoined>> getCourses(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();

        String studentId = student.getStudentId();

        List<CourseJoined> courses = courseRepo.show_course_joined(studentId);

        return ResponseEntity.ok(courses);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getStudent(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();
        return ResponseEntity.ok(student);
    }
}
