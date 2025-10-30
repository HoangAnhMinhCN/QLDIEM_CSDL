package com.qldiem.demo.DTO;

import lombok.Data;

@Data
public class StudentInCourseResponse {
    private String studentId;
    private String studentName;
    private String birthday;
    private String gender;
    private String joinDate;
}
