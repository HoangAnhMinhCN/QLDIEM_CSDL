package com.qldiem.demo.Controller;

import com.qldiem.demo.DataMapping.CourseJoined;
import com.qldiem.demo.DataMapping.CourseResponse;
import com.qldiem.demo.DataMapping.Student;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.StudentRepository;
import com.qldiem.demo.Security.CustomUserDetails;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private String getStudentId(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUserId();
    }

    // Xem profile
    @GetMapping("/me/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getStudentInfo(Authentication authentication) {
        String studentId = getStudentId(authentication);
        List<Student> result = studentRepository.show_student_info(studentId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result.getFirst());
    }

    // Xem các khóa học đã tham gia
    @GetMapping("/me/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseJoined>> getCourses(Authentication authentication) {
        String studentId = getStudentId(authentication);
        List<CourseJoined> courses = studentRepository.show_course_joined(studentId);
        return ResponseEntity.ok(courses);
    }



    //Xem tất cả khóa học
    @GetMapping("/courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courseResponses = courseRepository.getAllCourse();
        return ResponseEntity.ok(courseResponses);
    }

    //Xem khoa học chưa tham gia
    @GetMapping("/courses/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> getAllAvailableCourses(Authentication authentication) {
        String studentId = getStudentId(authentication);
        List<CourseResponse> courseResponses = courseRepository.findAvailableCoursesForStudent(studentId);
        return ResponseEntity.ok(courseResponses);
    }

    // Tìm kiếm khóa học
    @GetMapping("/courses/search")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponse>> searchCourse(@RequestParam String keyword) {
        List<CourseResponse> courseResponses = courseRepository.searchCourse(keyword);
        return ResponseEntity.ok(courseResponses);
    }

    // Xem thông tin khoa học
    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable String courseId) {
        return courseRepository.findCourseById(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //=============================
    //Đăng kí khóa
    @PostMapping("/courses/{courseId}/join")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> joinCourse(@PathVariable String courseId,
                                        Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            studentRepository.join_course(studentId, courseId, LocalDate.now().toString());
            return ResponseEntity.ok("Đăng ký thành công!");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Hủy khóa
    @DeleteMapping("/courses/{courseId}/delete")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId,
                                          Authentication authentication) {
        try {
            String studentId = getStudentId(authentication);
            studentRepository.leave_course(studentId, courseId);
            return ResponseEntity.ok("Hủy khóa thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
