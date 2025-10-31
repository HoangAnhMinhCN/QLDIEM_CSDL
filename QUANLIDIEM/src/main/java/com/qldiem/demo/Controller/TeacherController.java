package com.qldiem.demo.Controller;

import com.qldiem.demo.DTO.CourseResponse;
import com.qldiem.demo.DTO.CreateCourseRequest;
import com.qldiem.demo.DTO.StudentInCourseResponse;
import com.qldiem.demo.DTO.UpdateCourseRequest;
import com.qldiem.demo.Entity.Teacher;
import com.qldiem.demo.Service.AttendanceService;
import com.qldiem.demo.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private AttendanceService attendanceService;

    // profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Teacher> getProfile(Authentication authentication) {
        Teacher teacher = (Teacher) authentication.getPrincipal();
        return ResponseEntity.ok(teacher);
    }

    //Tạo khóa học
    @PostMapping("/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest req,
                                               Authentication authentication) {
        try {
            Teacher teacher = (Teacher) authentication.getPrincipal();
            CourseResponse  courseResponse = courseService.createCourseRepository(req, teacher.getTeacherId());
            return new ResponseEntity<>(courseResponse, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Lấy ds khóa của teacher
    @GetMapping("/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<CourseResponse>> getCourses(Authentication authentication) {
        Teacher teacher = (Teacher) authentication.getPrincipal();
        List<CourseResponse> courses = courseService.getCourseByTeacher(teacher.getTeacherId());
        return ResponseEntity.ok(courses);
    }

    //Lấy thông tin khóa
    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getCourseById(@PathVariable String courseId) {
        try {
            CourseResponse courseResponse = courseService.getCourseById(courseId);
            return ResponseEntity.ok(courseResponse);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //cập nhật khóa
    @PutMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateCourseById(@PathVariable String courseId,
                                              @RequestBody UpdateCourseRequest req,
                                              Authentication authentication) {
        try {
            Teacher teacher = (Teacher) authentication.getPrincipal();
            CourseResponse courseResponse = courseService.updateCourseRepository(req, courseId, teacher.getTeacherId());
            return ResponseEntity.ok(courseResponse);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Xóa khóa học
    @DeleteMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteCourseById(@PathVariable String courseId,
                                              Authentication authentication) {
        try {
            Teacher teacher = (Teacher) authentication.getPrincipal();
            courseService.deleteCourseRepository(courseId, teacher.getTeacherId());
            return ResponseEntity.ok("Xóa thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //==============================
    //Ds svien trong khóa
    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getStudentsInCourse(@PathVariable String courseId,
                                                 Authentication authentication) {
        try {
            Teacher teacher = (Teacher) authentication.getPrincipal();
            List<StudentInCourseResponse> students = attendanceService.getStudentsInCourse(courseId, teacher.getTeacherId());
            return ResponseEntity.ok(students);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Thêm svien vào khóa
    @PostMapping("/courses/{courseId}/students/{student_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> addStudentToCourse(@PathVariable String student_id,
                                                @PathVariable String courseId,
                                                Authentication authentication) {
        try {
            Teacher teacher = (Teacher) authentication.getPrincipal();
            attendanceService.addStudentInCourse(student_id, courseId, teacher.getTeacherId());
            return ResponseEntity.ok("Thêm thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Xóa svien
    @DeleteMapping("/courses/{courseId}/students/{student_id}")
    @PreAuthorize("hasRole('TEACHER')")
    public  ResponseEntity<?> removeStudentFromCourse(@PathVariable String courseId,
                                                      @PathVariable String student_id,
                                                      Authentication authentication) {
        try {
            Teacher teacher = (Teacher) authentication.getPrincipal();
            attendanceService.removeStudentInCourse(student_id, courseId, teacher.getTeacherId());
            return ResponseEntity.ok("Đã xóa thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
