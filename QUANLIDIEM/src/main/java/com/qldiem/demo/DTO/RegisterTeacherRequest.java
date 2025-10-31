package com.qldiem.demo.DTO;

import lombok.Data;

@Data
public class RegisterTeacherRequest {
    private String teacherId;
    private String teacherName;
    private String userName;
    private String userPassword;
    private String birthday;
    private String gender;
}
