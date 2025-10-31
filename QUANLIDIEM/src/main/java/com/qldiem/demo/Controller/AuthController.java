package com.qldiem.demo.Controller;

import com.qldiem.demo.DTO.JwtAuthResponse;
import com.qldiem.demo.DTO.LoginRequest;
import com.qldiem.demo.DTO.RegisterStudentRequest;
import com.qldiem.demo.DTO.RegisterTeacherRequest;
import com.qldiem.demo.Entity.Student;
import com.qldiem.demo.Entity.Teacher;
import com.qldiem.demo.Repository.StudentRepository;
import com.qldiem.demo.Repository.TeacherRepository;
import com.qldiem.demo.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = "";
        if (userDetails instanceof Student) {
            userId = ((Student) userDetails).getStudentId();
        }
        else if (userDetails instanceof Teacher) {
            userId = ((Teacher) userDetails).getTeacherId();
        }

        return ResponseEntity.ok(new JwtAuthResponse(
                token,
                userDetails.getUsername(),
                userId,
                userDetails.getAuthorities()
        ));
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest req) {
        if (studentRepository.existsByUserName(req.getUserName()) || teacherRepository.existsByUserName(req.getUserName())){
            return new ResponseEntity<>("Tên đăng nhập đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        Student student = new Student();
        student.setStudentId("student" + UUID.randomUUID().toString().replace("-", "").substring(0, 14));
        student.setUserName(req.getUserName());
        student.setUserPassword(passwordEncoder.encode(req.getUserPassword()));
        student.setStudentName(req.getStudentName());
        student.setBirthday(req.getBirthday());
        student.setGender(req.getGender());

        studentRepository.save(student);
        return new ResponseEntity<>("Đăng ký thành công!", HttpStatus.CREATED);
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody RegisterTeacherRequest req) {
        if (studentRepository.existsByUserName(req.getUserName()) ||
                teacherRepository.existsByUserName(req.getUserName())){
            return new ResponseEntity<>("Tên đăng nhập đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        Teacher teacher = new Teacher();
        teacher.setTeacherId("teacher" + UUID.randomUUID().toString().replace("-", "").substring(0, 14));
        teacher.setUserName(req.getUserName());
        teacher.setUserPassword(passwordEncoder.encode(req.getUserPassword()));
        teacher.setTeacherName(req.getTeacherName());
        teacher.setBirthday(req.getBirthday());
        teacher.setGender(req.getGender());

        teacherRepository.save(teacher);
        return new ResponseEntity<>("Đăng ký thành công!", HttpStatus.CREATED);
    }
}
