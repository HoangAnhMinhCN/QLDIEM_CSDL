package com.qldiem.demo.DTO;

import lombok.Data;

@Data
public class CourseResponse {
    private String courseId;
    private String courseName;
    private String teacherId;
    private String teacherName;
    private String startDate;
    private Integer totalStudents;
}
