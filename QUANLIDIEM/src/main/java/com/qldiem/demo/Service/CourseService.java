package com.qldiem.demo.Service;

import com.qldiem.demo.DTO.CourseResponse;
import com.qldiem.demo.DTO.CreateCourseRequest;
import com.qldiem.demo.DTO.UpdateCourseRequest;
import com.qldiem.demo.Entity.Course;
import com.qldiem.demo.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    //Tạo khóa học
    public CourseResponse createCourseRepository(CreateCourseRequest req, String teacherId) {
        Course course = new Course();
        course.setCourseId("course" + UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        course.setCourseName(req.getCourseName());
        course.setTeacherId(teacherId);
        course.setStartDate(req.getStartDate());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    //Cập nhật khóa
    public CourseResponse updateCourseRepository(UpdateCourseRequest req, String courseId, String teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));

        if (!course.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền sửa khóa học này!");
        }

        if (req.getCourseName() != null) {
            course.setCourseName(req.getCourseName());
        }

        if (req.getStartDate() != null) {
            course.setStartDate(req.getStartDate());
        }

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    //Xóa khóa học
    public void deleteCourseRepository(String courseId, String teacherId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));

        if (!course.getTeacherId().equals(teacherId)) {
            throw new RuntimeException("Bạn không có quyền xóa khóa học này!");
        }

        courseRepository.delete(course);
    }

    //Ds khóa học của teacher
    public List<CourseResponse> getCourseByTeacher(String teacherId) {
        List<Course> courseList = courseRepository.findByTeacherId(teacherId);
        return courseList.stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    //Lấy tất cả khóa học
    public  List<CourseResponse> getAllCourse(){
        List<Course> courseList = courseRepository.findAll();
        return courseList.stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    //Lấy khóa học chưa tham gia
    public List<CourseResponse> getAvailableCourses(String studentId) {
        List<Course> courseList = courseRepository.findAvailableCoursesForStudent(studentId);
        return courseList.stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    //Tìm kiếm khóa học theo tên
    public List<CourseResponse> searchCourse(String keyword) {
        List<Course> courseList = courseRepository.findByCourseNameContainingIgnoreCase(keyword);
        return courseList.stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    //Thông tin 1 khóa học
    public CourseResponse getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học!"));
        return mapToCourseResponse(course);
    }

    //Map Course entity sang CourseResponse DTO
    private CourseResponse mapToCourseResponse(Course course) {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setCourseId(course.getCourseId());
        courseResponse.setCourseName(course.getCourseName());
        courseResponse.setTeacherId(course.getTeacherId());
        courseResponse.setStartDate(course.getStartDate());

        if (course.getTeacher() != null) {
            courseResponse.setTeacherName(course.getTeacher().getTeacherName());
        }

        return courseResponse;
    }
}
