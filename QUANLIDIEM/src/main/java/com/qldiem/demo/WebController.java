package com.qldiem.demo;

import com.qldiem.demo.Relations.CourseJoined;
import com.qldiem.demo.Repository.StudentRepository;
import com.qldiem.demo.Repository.TeacherRepository;

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
//Dua du lieu len can url 
@RestController
@RequestMapping("/api")
public class WebController {
    //Muon dung cac ham lien quan den student hay teacher thi dung 2 bien nay
    @Autowired
    public StudentRepository studentRepository;
    @Autowired
    public TeacherRepository teacherRepository;

    //Tra ve danh sach cac lop ma hoc sinh dang hoc
    //test case: http://localhost:8080/api/student/courses/studentf38677fa6b
    @GetMapping("/student/courses/{student_id}")
    public List<CourseJoined> getCourseJoined(@PathVariable String student_id){
        return studentRepository.show_course_joined(student_id);
    }
}
