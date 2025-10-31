package com.qldiem.demo.WebApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qldiem.demo.Repository.AuthRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    public AuthRepository authRepository;

    
}
