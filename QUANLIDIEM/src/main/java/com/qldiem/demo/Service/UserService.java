package com.qldiem.demo.Service;

import com.qldiem.demo.Repository.StudentRepository;
import com.qldiem.demo.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = studentRepository.findByUserName(username).orElse(null);

        if (user == null) {
            user = teacherRepository.findByUserName(username).orElse(null);
        }

        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng với username: " + username);
        }

        return user;
    }

}
