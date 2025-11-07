package com.qldiem.demo.Controller;

import com.qldiem.demo.DTO.CreateExamRequest;
import com.qldiem.demo.DTO.UpdateExamRequest;
import com.qldiem.demo.DTO.UpdateScoreRequest;
import com.qldiem.demo.DataMapping.*;
import com.qldiem.demo.DTO.CreateCourseRequest;
import com.qldiem.demo.DTO.UpdateCourseRequest;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.ExamRepository;
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

    @Autowired
    private ExamRepository examRepository;

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

    // ============================== EXAM MANAGEMENT ==============================
    // tạo exam cho course
    @PostMapping("/courses/{courseId}/exams")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createExam(@PathVariable String courseId,
                                        @RequestBody CreateExamRequest req,
                                        Authentication authentication) {
        try {
        // teacher_id is derived from the authenticated user; create_exam no longer needs teacher_id param
        teacherRepository.create_exam(
            req.getExamName(),
            courseId,
            req.getExamDate(),
            java.time.LocalDate.now().toString()
        );
            return new ResponseEntity<>("Tạo bài kiểm tra thành công!", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // xem ds exam theo course
    @GetMapping("/courses/{courseId}/exams")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<ExamResponse>> getExamsByCourse(@PathVariable String courseId){
        try {
            List<ExamResponse> exams = examRepository.show_course_exams(courseId);
            return ResponseEntity.ok(exams);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //xem thông tin exam
    @GetMapping("/exams/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getExamById(@PathVariable String examId) {
        return examRepository.get_exam_by_id(examId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //sửa thông tin exam
    @PutMapping("/exams/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateExam(@PathVariable String examId,
                                        @RequestBody UpdateExamRequest req,
                                        Authentication authentication) {
        try {
            String teacherId = getTeacherId(authentication);
            teacherRepository.update_exam(
                    examId,
                    req.getExamName(),
                    req.getExamDate(),
                    teacherId
            );
            return ResponseEntity.ok("Cập nhật bài kiểm tra thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // xóa bài kiểm tra
    @DeleteMapping("/exams/{examId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> deleteExam(@PathVariable String examId,
                                        Authentication authentication) {
        try {
            String teacherId = getTeacherId(authentication);
            teacherRepository.delete_exam(examId, teacherId);
            return ResponseEntity.ok("Xóa bài kiểm tra thành công!");
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // xem điểm bài kiểm tra
    @GetMapping("/exams/{examId}/scores")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getExamScores(@PathVariable String examId) {
        try {
            List<ExamScores> scores = examRepository.show_exam_scores(examId);
            return ResponseEntity.ok(scores);
        }
        catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // cập nhật điểm bài kiểm tra
    @PutMapping("/exams/{examId}/scores/{studentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> updateExamScore(
            @PathVariable String examId,
            @PathVariable String studentId,
            @RequestBody UpdateScoreRequest req,
            Authentication authentication) {
        try {
            String teacherId = getTeacherId(authentication);
            teacherRepository.update_score(examId, studentId, String.valueOf(req.getScore()), teacherId);
            return ResponseEntity.ok("Cập nhật điểm thành công!");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
