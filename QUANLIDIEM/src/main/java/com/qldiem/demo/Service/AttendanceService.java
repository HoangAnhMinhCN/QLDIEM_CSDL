package com.qldiem.demo.Service;

import com.qldiem.demo.DTO.StudentInCourseResponse;
import com.qldiem.demo.Entity.Attendance;
import com.qldiem.demo.Entity.Course;
import com.qldiem.demo.Entity.Student;
import com.qldiem.demo.Repository.AttendanceRepository;
import com.qldiem.demo.Repository.CourseRepository;
import com.qldiem.demo.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    //Tham gia khóa học
    public void joinCourse(String studentId, String courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại!"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Không tồn tại học sinh này!"));

        if (attendanceRepository.existsByStudentIdAndCourseId(studentId,courseId)){
            throw new RuntimeException("Bạn đã tham gia khóa học này!");
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setJoinDate(LocalDate.now().toString());

        attendanceRepository.save(attendance);
    }

    //Rời khóa học
    public void leaveCourse(String studentId, String courseId){
        Attendance attendance = attendanceRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Bạn chưa tham gia khóa học!"));

        attendanceRepository.delete(attendance);
    }

    //Teacher xem dánh sách hs trong khóa
    public List<StudentInCourseResponse> getStudentsInCourse(String courseId, String teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại!"));

        if (!course.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền truy cập chức năng này!");
        }

        List<Attendance> attendances = attendanceRepository.findByCourseId(courseId);

        return attendances.stream()
                .map(this::mapToStudentInCourseResponse)
                .collect(Collectors.toList());
    }

    //Teacher thêm svien vào khóa
    public void addStudentInCourse(String studentId, String courseId, String teacherId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại!"));

        if (!course.getTeacherId().equals(teacherId)){
            throw new RuntimeException("Bạn không có quyền quản lí khóa học này!");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại!"));

        if (attendanceRepository.existsByStudentIdAndCourseId(studentId,courseId)){
            throw new RuntimeException("Sinh viên đã tham gia khóa học này rồi!");
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setCourseId(courseId);
        attendance.setJoinDate(LocalDate.now().toString());

        attendanceRepository.save(attendance);
    }

    //Teacher xóa sinh viên
    public void removeStudentInCourse(String studentId, String courseId, String teacherId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Khóa học không tồn tại!"));

        if (!course.getTeacherId().equals(teacherId)){
            throw new RuntimeException("Bạn không có quyền quản lý khóa học này!");
        }

        Attendance attendance = attendanceRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Sinh viên chưa tham gia khóa học này!"));

        attendanceRepository.delete(attendance);
    }

    private StudentInCourseResponse mapToStudentInCourseResponse(Attendance attendance){
        StudentInCourseResponse studentInCourseResponse = new StudentInCourseResponse();

        Student student = studentRepository.findById(attendance.getStudentId()).orElse(null);
        if (student != null){
            studentInCourseResponse.setStudentId(student.getStudentId());
            studentInCourseResponse.setStudentName(student.getStudentName());
            studentInCourseResponse.setBirthday(student.getBirthday());
            studentInCourseResponse.setGender(student.getGender());
        }

        studentInCourseResponse.setJoinDate(attendance.getJoinDate());

        return studentInCourseResponse;
    }
}
