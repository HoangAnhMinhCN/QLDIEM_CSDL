package com.qldiem.demo.Controller;

import com.qldiem.demo.DTO.JwtAuthResponse;
import com.qldiem.demo.DTO.LoginRequest;
import com.qldiem.demo.DTO.RegisterStudentRequest;
import com.qldiem.demo.DTO.RegisterTeacherRequest;
import com.qldiem.demo.Repository.StudentRepository;
import com.qldiem.demo.Repository.TeacherRepository;
import com.qldiem.demo.Security.CustomUserDetails;
import com.qldiem.demo.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUserId();

        return ResponseEntity.ok(new JwtAuthResponse(
                token,
                userDetails.getUsername(),
                userId,
                userDetails.getAuthorities()
        ));
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterStudentRequest req) {
        if (studentRepository.check_username_exist(req.getUserName()) > 0
                || teacherRepository.check_username_exist(req.getUserName()) > 0){
            return new ResponseEntity<>("Tên đăng nhập đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        String studentId = "student" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);

        studentRepository.create_student(
                studentId,
                req.getStudentName(),
                req.getUserName(),
                passwordEncoder.encode(req.getUserPassword()),
                req.getBirthday(),
                req.getGender()
        );

        return new ResponseEntity<>("Đăng ký thành công!", HttpStatus.CREATED);
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody RegisterTeacherRequest req) {
        if (studentRepository.check_username_exist(req.getUserName()) > 0
                || teacherRepository.check_username_exist(req.getUserName()) > 0){
            return new ResponseEntity<>("Tên đăng nhập đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        String teacherId = "teacher" + UUID.randomUUID().toString().replace("-", "").substring(0, 14);

        teacherRepository.create_teacher(
                teacherId,
                req.getTeacherName(),
                req.getUserName(),
                passwordEncoder.encode(req.getUserPassword()),
                req.getBirthday(),
                req.getGender()
        );

        return new ResponseEntity<>("Đăng ký thành công!", HttpStatus.CREATED);
    }
}
