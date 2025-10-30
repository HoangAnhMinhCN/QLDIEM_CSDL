package com.qldiem.demo.DTO;

import lombok.Data;

@Data
public class RegisterStudentRequest {
    private String studentId;
    private String studentName;
    private String userName;
    private String userPassword;
    private String birthday;
    private String gender;
}
