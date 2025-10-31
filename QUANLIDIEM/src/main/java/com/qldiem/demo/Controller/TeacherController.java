package com.qldiem.demo.Controller;

import com.qldiem.demo.DataMapping.CourseResponse;
import com.qldiem.demo.DataMapping.CourseTeached;
import com.qldiem.demo.DataMapping.StudentList;
import com.qldiem.demo.DataMapping.Teacher;
import com.qldiem.demo.DTO.CreateCourseRequest;
import com.qldiem.demo.DTO.UpdateCourseRequest;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.TeacherRepository;
import com.qldiem.demo.Security.CustomUserDetails;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;

    private String getTeacherId(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getUserId();
    }

    // profile
    @GetMapping("/profile")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Teacher> getProfile(Authentication authentication) {
        String teacherId = getTeacherId(authentication);
        List<Teacher> result = teacherRepository.show_teacher_info(teacherId);

        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.getFirst());
    }

    //Tạo khóa học
    @PostMapping("/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createCourse(@RequestBody CreateCourseRequest req,
                                               Authentication authentication) {
        try {
            String teacherId = getTeacherId(authentication);
            teacherRepository.create_course(
                    req.getCourseName(),
                    teacherId,
                    req.getStartDate()
            );

            return new ResponseEntity<>("Tạo khóa học thành công!", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Lấy ds khóa của teacher
    @GetMapping("/courses")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<CourseTeached>> getCourses(Authentication authentication) {
        String teacherId = getTeacherId(authentication);
        List<CourseTeached> courses = teacherRepository.show_course_teached(teacherId);
        return ResponseEntity.ok(courses);
    }

    //Lấy thông tin khóa
    @GetMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getCourseById(@PathVariable String courseId) {
        return courseRepository.findCourseById(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //cập nhật khóa
    @PutMapping("/courses/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateCourseById(@PathVariable String courseId,
                                              @RequestBody UpdateCourseRequest req,
                                              Authentication authentication) {
        try {
            String teacherId = getTeacherId(authentication);
            teacherRepository.update_course(
                courseId,
                req.getCourseName(),
                req.getStartDate(),
                teacherId
            );
            return ResponseEntity.ok("Cập nhật thành công!");
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
            String teacherId = getTeacherId(authentication);
            teacherRepository.delete_course(courseId, teacherId);
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
            List<StudentList> students = courseRepository.show_course_StudentLists(courseId);
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
            String teacherId = getTeacherId(authentication);
            teacherRepository.add_student_to_course(student_id, courseId, teacherId);
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
            String teacherId = getTeacherId(authentication);
            teacherRepository.remove_student_from_course(student_id, courseId, teacherId);
            return ResponseEntity.ok("Đã xóa thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
