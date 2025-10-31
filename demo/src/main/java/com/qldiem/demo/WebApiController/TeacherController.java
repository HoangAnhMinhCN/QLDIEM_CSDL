package com.qldiem.demo.WebApiController;

import com.qldiem.demo.DataMapping.CourseTeached;
import com.qldiem.demo.DataMapping.Teacher;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.TeacherRepository;

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
@RequestMapping("/api/teacher/{teacher_id}")
public class TeacherController {
    @Autowired
    public TeacherRepository teacherRepository;
    @Autowired
    public CourseRepository courseRepository;
    
    //Phan thong tin giao vien
    @GetMapping("/info")
    public ResponseEntity<List<Teacher>> getTeacherInfo(@PathVariable("teacher_id") String teacherId) {
        List<Teacher> result = teacherRepository.show_teacher_info(teacherId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    //Phan cap nhat thong tin giao vien 
    @PostMapping("/update")
    public ResponseEntity<String> updateTeacher(@RequestBody Map<String, Object> requestData) {
        try {
            String teacherId = (String) requestData.get("teacher_id");
            String teacherName = (String) requestData.get("teacher_name");
            String userName = (String) requestData.get("user_name");
            String userPassword = (String) requestData.get("user_password");
            String birthday = (String) requestData.get("birthday");
            String gender = (String) requestData.get("gender");

            teacherRepository.update_teacher(teacherId, teacherName, userName, userPassword, birthday, gender);
            return ResponseEntity.ok("Teacher information updated");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating teacher: " + e.getMessage());
        }
    }

    //Phan hien danh sach lop dang day
    @GetMapping("/courses")
    public ResponseEntity<List<CourseTeached>> getCoursesTaught(@PathVariable("teacher_id") String teacherId) {
        try {
            List<CourseTeached> courses = teacherRepository.show_course_teached(teacherId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    //Phan tao lop moi
    @PostMapping("/course/create")
    @Transactional
    public ResponseEntity<String> createCourse(@RequestBody Map<String, Object> requestData, @PathVariable("teacher_id") String teacherId) {
        try {
            String courseName = (String) requestData.get("course_name");
            String startDate = LocalDate.now().toString();
            teacherRepository.create_course(courseName, teacherId, startDate);
            return ResponseEntity.ok("Course "+courseName+ " created");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating course: " + e.getMessage());
        }
    }

    //Phan cap nhat lai ten lop
    @PostMapping("/course/update")
    @Transactional
    public ResponseEntity<String> updateCourse(@RequestBody Map<String, Object> requestData) {
        try {
            String courseId = (String) requestData.get("course_id");
            String courseName = (String) requestData.get("course_name");
            teacherRepository.update_course(courseId, courseName);
            return ResponseEntity.ok("Course "+courseId+" updated");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error updating course: " + e.getMessage());
        }
    }

    //Phan xoa lop
    @PostMapping("/course/delete")
    @Transactional
    public ResponseEntity<String> deleteCourse(@RequestBody Map<String, Object> requestData) {
        try {
            String courseId = (String) requestData.get("course_id");
            teacherRepository.delete_course(courseId);
            return ResponseEntity.ok("Course "+courseId+" deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting course: " + e.getMessage());
        }
    }
}
