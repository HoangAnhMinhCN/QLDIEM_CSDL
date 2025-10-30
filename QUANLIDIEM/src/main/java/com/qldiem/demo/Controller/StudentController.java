package com.qldiem.demo.Controller;


import com.qldiem.demo.DTO.CourseResponse;
import com.qldiem.demo.Relations.CourseJoined;
import com.qldiem.demo.Entity.Student;
import com.qldiem.demo.Repository.CourseStoredProcRepository;
import com.qldiem.demo.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private CourseStoredProcRepository courseRepo;

    @Autowired
    private CourseService  courseService;

    // Xem các khóa học đã tham gia
    @GetMapping("/me/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseJoined>> getCourses(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();
        String studentId = student.getStudentId();
        List<CourseJoined> courses = courseRepo.show_course_joined(studentId);
        return ResponseEntity.ok(courses);
    }

    // Xem profile
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getStudent(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();
        return ResponseEntity.ok(student);
    }

    //Xem tất cả khóa học
    @GetMapping("/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courseResponses = courseService.getAllCourse();
        return ResponseEntity.ok(courseResponses);
    }

    //Xem khoa học chưa tham gia
    @GetMapping("/courses/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> getAllAvailableCourses(Authentication authentication) {
        Student student = (Student) authentication.getPrincipal();
        List<CourseResponse> courseResponses = courseService.getAvailableCourses(student.getStudentId());
        return ResponseEntity.ok(courseResponses);
    }

    // Tìm kiếm khóa học
    @GetMapping("/courses/search")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> searchCourse(@RequestParam String keyword) {
        List<CourseResponse> courseResponses = courseService.searchCourse(keyword);
        return ResponseEntity.ok(courseResponses);
    }

    // Xem thông tin khoa học
    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable String courseId) {
        try {
            CourseResponse courseResponse = courseService.getCourseById(courseId);
            return ResponseEntity.ok(courseResponse);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
