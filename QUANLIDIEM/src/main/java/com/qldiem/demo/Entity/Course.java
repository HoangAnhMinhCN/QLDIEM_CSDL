package com.qldiem.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {

    @Id
    @Column(name = "course_id")
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "teacher_id", nullable = false)
    private String teacherId;

    @Column(name = "start_date")
    private String startDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", insertable = false, updatable = false)
    private Teacher teacher;
}
