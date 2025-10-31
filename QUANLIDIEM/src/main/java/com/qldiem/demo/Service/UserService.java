package com.qldiem.demo.Service;

import com.qldiem.demo.DataMapping.Student;
import com.qldiem.demo.DataMapping.Teacher;
import com.qldiem.demo.Repository.StudentRepository;
import com.qldiem.demo.Repository.TeacherRepository;
import com.qldiem.demo.Security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Teacher> teacherOpt = teacherRepository.find_teacher_by_username(username);

        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));

            return new CustomUserDetails(
                    teacher.getUserName(),
                    teacher.getUserPassword(),
                    teacher.getTeacherId(),
                    authorities
            );
        }


        Optional<Student> studentOpt = studentRepository.find_student_by_username(username);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));

            return new CustomUserDetails(
                    student.getUserName(),
                    student.getUserPassword(),
                    student.getStudentId(),
                    authorities
            );
        }


        throw new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username);
    }
}
