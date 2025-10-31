-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: qldiem_csdl
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- =====================================================
-- CREATE AND USE DATABASE
-- =====================================================
CREATE DATABASE IF NOT EXISTS qldiem_csdl;
USE qldiem_csdl;

--
-- Table structure for table `teacher`
--

DROP TABLE IF EXISTS `teacher`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
                           `teacher_id` varchar(255) NOT NULL,
                           `teacher_name` varchar(255) NOT NULL,
                           `user_name` varchar(255) NOT NULL,
                           `user_password` varchar(255) NOT NULL,
                           `birthday` varchar(255) DEFAULT NULL,
                           `gender` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teacher`
--

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('t2','Nguyen Van Quy','quynv','123','1980-06-07','nu'),('t3','Hoàng Văn Ngọc','Ngọc','123456789','1990-03-03','nam'),('teacher9d42ad7fd5','Trần Thu Hương','huong','huong1990','1990-09-08','nu'),('teacherd96127310e','Trần Thu Hoài','HoaiThu','hoaithu99999','1989-08-08','nu');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
                           `student_id` varchar(255) NOT NULL,
                           `student_name` varchar(255) NOT NULL,
                           `user_name` varchar(255) NOT NULL,
                           `user_password` varchar(255) NOT NULL,
                           `birthday` varchar(255) DEFAULT NULL,
                           `gender` varchar(255) DEFAULT NULL,
                           PRIMARY KEY (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('exam7ddc49e8bf','Hoàng Văn Bắc','bacvanhoang','eeeeee','2005-12-12','nam'),('s2','hoang van b','random','123456','2000-01-04','nam'),('studentd55341ebad','Nguyễn Thị Ngọc Diệp','diepa','asdfgh','2005-03-12','nu'),('studentf14ac13566','Trần Thu Hương','huong','huong1990','2004-10-08','nu'),('studentf38677fa6b','Nguyễn Thị Hằng','hhhhhaaa','zzzzz','2004-03-01','nu');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
                          `course_id` varchar(255) NOT NULL,
                          `course_name` varchar(255) NOT NULL,
                          `teacher_id` varchar(255) NOT NULL,
                          `start_date` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`course_id`),
                          KEY `teacher_id` (`teacher_id`),
                          CONSTRAINT `course_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('c3','Calculus','t3','2025-08-16'),('course124de93bb6','Hệ Điều Hành - Nhóm 1','teacher9d42ad7fd5',NULL),('course199cac2670','Mạng máy tính','teacherd96127310e','2025-08-20'),('course257adf1050','Cơ sở dữ liệu','teacher9d42ad7fd5','2025-08-10'),('course28c9f8459b','Hệ Điều Hành','teacher9d42ad7fd5',NULL),('course7099d558ca','Cơ sở dữ liệu - Nhóm 2','teacher9d42ad7fd5','2025-08-11');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `student_id` varchar(255) NOT NULL,
                              `course_id` varchar(255) NOT NULL,
                              `join_date` date DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `student_id` (`student_id`),
                              KEY `course_id` (`course_id`),
                              CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE,
                              CONSTRAINT `attendance_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (4,'studentf38677fa6b','course199cac2670','2025-08-13'),(5,'studentd55341ebad','course199cac2670','2025-08-13');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam` (
                        `exam_id` varchar(255) NOT NULL,
                        `exam_name` varchar(255) NOT NULL,
                        `teacher_id` varchar(255) NOT NULL,
                        `course_id` varchar(255) NOT NULL,
                        `exam_date` varchar(255) DEFAULT NULL,
                        `created_date` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`exam_id`),
                        KEY `teacher_id` (`teacher_id`),
                        KEY `course_id` (`course_id`),
                        CONSTRAINT `exam_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`) ON DELETE CASCADE,
                        CONSTRAINT `exam_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exam`
--

LOCK TABLES `exam` WRITE;
/*!40000 ALTER TABLE `exam` DISABLE KEYS */;
INSERT INTO `exam` VALUES ('exam0f23c50d3d','Kiểm tra 15 phút','teacherd96127310e','course199cac2670','2025-12-06',NULL),('examd5a6e7b0a6','Kiểm tra 15 phút lần 2','teacherd96127310e','course199cac2670','2025-12-06','2025-09-20');
/*!40000 ALTER TABLE `exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `score`
--

DROP TABLE IF EXISTS `score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `score` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `exam_id` varchar(255) NOT NULL,
                         `teacher_id` varchar(255) NOT NULL,
                         `course_id` varchar(255) NOT NULL,
                         `student_id` varchar(255) NOT NULL,
                         `score` int NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `exam_id` (`exam_id`),
                         KEY `teacher_id` (`teacher_id`),
                         KEY `student_id` (`student_id`),
                         KEY `course_id` (`course_id`),
                         CONSTRAINT `score_ibfk_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`exam_id`) ON DELETE CASCADE,
                         CONSTRAINT `score_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`) ON DELETE CASCADE,
                         CONSTRAINT `score_ibfk_3` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE,
                         CONSTRAINT `score_ibfk_4` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `score`
--

LOCK TABLES `score` WRITE;
/*!40000 ALTER TABLE `score` DISABLE KEYS */;
INSERT INTO `score` VALUES (1,'exam0f23c50d3d','teacherd96127310e','course199cac2670','studentf38677fa6b',0),(2,'exam0f23c50d3d','teacherd96127310e','course199cac2670','studentd55341ebad',10),(4,'examd5a6e7b0a6','teacherd96127310e','course199cac2670','studentf38677fa6b',0),(5,'examd5a6e7b0a6','teacherd96127310e','course199cac2670','studentd55341ebad',0);
/*!40000 ALTER TABLE `score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dummy_entity`
--

DROP TABLE IF EXISTS `dummy_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dummy_entity` (
                                `id` varchar(255) NOT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dummy_entity`
--

LOCK TABLES `dummy_entity` WRITE;
/*!40000 ALTER TABLE `dummy_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `dummy_entity` ENABLE KEYS */;
UNLOCK TABLES;

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- tìm student bằng username login
DROP PROCEDURE IF EXISTS `find_student_by_username`;
DELIMITER $$
CREATE PROCEDURE `find_student_by_username`(IN username_param VARCHAR(255))
BEGIN
    SELECT * FROM student WHERE user_name = username_param;
END$$
DELIMITER ;

-- tìm teacher bằng username login
DROP PROCEDURE IF EXISTS `find_teacher_by_username`;
DELIMITER $$
CREATE PROCEDURE `find_teacher_by_username`(IN username_param VARCHAR(255))
BEGIN
    SELECT * FROM teacher WHERE user_name = username_param;
END$$
DELIMITER ;

-- đăng kí student mới
DROP PROCEDURE IF EXISTS `create_student`;
DELIMITER $$
CREATE PROCEDURE `create_student`(
    IN student_id_param VARCHAR(255),
    IN student_name_param VARCHAR(255),
    IN user_name_param VARCHAR(255),
    IN user_password_param VARCHAR(255),
    IN birthday_param VARCHAR(255),
    IN gender_param VARCHAR(255))
BEGIN
    INSERT INTO student(student_id, student_name, user_name, user_password, birthday, gender)
    VALUES (student_id_param, student_name_param, user_name_param, user_password_param, birthday_param, gender_param);
END$$
DELIMITER ;

-- đăng kí teacher mới
DROP PROCEDURE IF EXISTS `create_teacher`;
DELIMITER $$
CREATE PROCEDURE `create_teacher`(
    IN teacher_id_param VARCHAR(255),
    IN teacher_name_param VARCHAR(255),
    IN user_name_param VARCHAR(255),
    IN user_password_param VARCHAR(255),
    IN birthday_param VARCHAR(255),
    IN gender_param VARCHAR(255))
BEGIN
    INSERT INTO teacher(teacher_id, teacher_name, user_name, user_password, birthday, gender)
    VALUES (teacher_id_param, teacher_name_param, user_name_param, user_password_param, birthday_param, gender_param);
END$$
DELIMITER ;

-- Xem thông tin teacher
DROP PROCEDURE IF EXISTS `show_teacher_info`;
DELIMITER $$
CREATE PROCEDURE `show_teacher_info`(IN teacher_id_param VARCHAR(255))
BEGIN
    SELECT * FROM teacher WHERE teacher_id = teacher_id_param;
END$$
DELIMITER ;

-- Cập nhật thông tin teacher
DROP PROCEDURE IF EXISTS `update_teacher`;
DELIMITER $$
CREATE PROCEDURE `update_teacher`(
    IN teacher_id_param VARCHAR(255),
    IN teacher_name_param VARCHAR(255),
    IN user_name_param VARCHAR(255),
    IN user_password_param VARCHAR(255),
    IN birthday_param VARCHAR(255),
    IN gender_param VARCHAR(255))
BEGIN
    UPDATE teacher
    SET
        teacher_name = teacher_name_param,
        user_name = user_name_param,
        user_password = user_password_param,
        birthday = birthday_param,
        gender = gender_param
    WHERE teacher_id = teacher_id_param;
END$$
DELIMITER ;

-- lấy tất cả course
DROP PROCEDURE IF EXISTS `get_all_course`;
DELIMITER $$
CREATE PROCEDURE `get_all_course`()
BEGIN
    SELECT c.*, t.teacher_name
    FROM course c
             JOIN teacher t ON c.teacher_id = t.teacher_id;
END$$
DELIMITER ;

-- lấy course theo id
DROP PROCEDURE IF EXISTS `get_course_by_id`;
DELIMITER $$
CREATE PROCEDURE `get_course_by_id`(IN course_id_param VARCHAR(255))
BEGIN
    SELECT c.*, t.teacher_name
    FROM course c
             JOIN teacher t ON c.teacher_id = t.teacher_id
    WHERE c.course_id = course_id_param;
END$$
DELIMITER ;

-- search course
DROP PROCEDURE IF EXISTS `search_course`;
DELIMITER $$
CREATE PROCEDURE `search_course`(IN keyword_param VARCHAR(255))
BEGIN
    SELECT c.*, t.teacher_name
    FROM course c
             JOIN teacher t  ON c.teacher_id = t.teacher_id
    WHERE c.course_name LIKE CONCAT('%', keyword_param, '%');
END$$
DELIMITER ;

-- Tìm course cho student
DROP PROCEDURE IF EXISTS `find_available_courses_for_student`;
DELIMITER $$
CREATE PROCEDURE `find_available_courses_for_student`(IN student_id_param VARCHAR(255))
BEGIN
    SELECT c.*, t.teacher_name
    FROM course c
             JOIN teacher t  ON c.teacher_id = t.teacher_id
             LEFT JOIN attendance a ON c.course_id = a.course_id AND a.student_id = student_id_param
    WHERE a.id IS NULL;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `create_course`;
DELIMITER $$
CREATE PROCEDURE `create_course`(
    IN p_course_name VARCHAR(255),
    IN p_teacher_id VARCHAR(255),
    IN p_start_date VARCHAR(255)
)
BEGIN
    -- tự tạo Id
    DECLARE new_course_id VARCHAR(255);
    SET new_course_id = CONCAT('course', SUBSTRING(REPLACE(UUID(), '-', ''), 1, 10));
    -- thêm course
    INSERT INTO course (course_id, course_name, teacher_id, start_date)
    VALUES (new_course_id, p_course_name, p_teacher_id, p_start_date);
    -- show thông tin vừa tạo
    SELECT
        course_id AS courseId,
        course_name AS courseName,
        start_date AS startDate
    FROM course
    WHERE course_id = new_course_id;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `show_course_teached`;
DELIMITER $$
CREATE PROCEDURE `show_course_teached`(IN teacher_id_param VARCHAR(255))
BEGIN
    SELECT
        course_id AS courseId,
        course_name AS courseName,
        start_date AS startDate
    FROM course
    WHERE teacher_id = teacher_id_param;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `update_course`;
DELIMITER $$
CREATE PROCEDURE `update_course`(
    IN p_course_id VARCHAR(255),
    IN p_course_name VARCHAR(255),
    IN p_start_date VARCHAR(255),
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE course_owner VARCHAR(255);
    -- ktra chủ khóa
    SELECT teacher_id INTO course_owner
    FROM course
    WHERE course_id = p_course_id;

    IF course_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khóa học không tồn tại!';
    ELSEIF course_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền sửa khóa học này!';
    ELSE
        -- cập nhật khóa
        UPDATE course
        SET
            course_name = p_course_name,
            start_date = p_start_date
        WHERE course_id = p_course_id;

        -- Hiện thông tin cập nhật
        SELECT
            course_id AS courseId,
            course_name AS courseName,
            start_date AS startDate
        FROM course
        WHERE course_id = p_course_id;
    END IF;
END$$
DELIMITER ;

-- ds student trong course
DROP PROCEDURE IF EXISTS `show_course_studentList`;
DELIMITER $$
CREATE PROCEDURE `show_course_studentList`(IN course_id_param VARCHAR(255))
BEGIN
    SELECT
        s.student_id AS StudentId,
        s.student_name AS StudentName
    FROM attendance a
    JOIN student s ON a.student_id = s.student_id
    WHERE a.course_id = course_id_param;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `delete_course`;
DELIMITER $$
CREATE PROCEDURE `delete_course`(
    IN p_course_id VARCHAR(255),
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE course_owner VARCHAR(255);

    -- Ktra chủ khóa học
    SELECT teacher_id INTO course_owner
    FROM course
    WHERE course_id = p_course_id;

    IF course_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khóa học không tồn tại!';
    ELSEIF course_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền sửa khóa học này!';
    ELSE
        DELETE FROM course
        WHERE course_id = p_course_id;
    END IF;
END$$
DELIMITER ;

-- teacher thêm student vào khóa
DROP PROCEDURE IF EXISTS `add_student_to_course`;
DELIMITER $$
CREATE PROCEDURE `add_student_to_course`(
    IN p_student_id VARCHAR(255),
    IN p_course_id VARCHAR(255),
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE course_owner VARCHAR(255);
    DECLARE student_exits INT;
    DECLARE joined INT;

    SELECT teacher_id INTO course_owner
    FROM course
    WHERE course_id = p_course_id;

    -- ktra chủ khóa học
    IF course_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khóa học không tồn tại!';
    ELSEIF course_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền sửa khóa học này!';
    END IF;

    -- ktra hs có tồn tại không
    SELECT COUNT(*) INTO student_exits
    FROM student
    WHERE student_id = p_student_id;

    IF student_exits = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sinh viên không tồn tại!';
    END IF;

    -- ktra hs có trong khóa chưa
    SELECT COUNT(*) INTO joined
    FROM attendance
    WHERE student_id = p_student_id AND course_id = p_course_id;

    IF joined > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sinh viên đã tham gia khóa học!';
    END IF;

    INSERT INTO attendance(student_id, course_id, join_date)
    VALUES (p_student_id, p_course_id, CURDATE());

END$$
DELIMITER ;

-- teacher xóa student khỏi course
DROP PROCEDURE IF EXISTS `remove_student_from_course`;
DELIMITER $$
CREATE PROCEDURE `remove_student_from_course`(
    IN p_student_id VARCHAR(255),
    IN p_course_id VARCHAR(255),
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE course_owner VARCHAR(255);
    DECLARE attendence_id INT;

    SELECT teacher_id INTO course_owner
    FROM course
    WHERE course_id = p_course_id;

    -- ktra chủ khóa học
    IF course_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khóa học không tồn tại!';
    ELSEIF course_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền sửa khóa học này!';
    END IF;

    -- ktra hs có trong khóa chưa
    SELECT id INTO attendence_id
    FROM attendance
    WHERE student_id = p_student_id AND course_id = p_course_id;

    IF attendence_id IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Sinh viên chưa tham gia khóa học này!';
    END IF;

    DELETE FROM attendance WHERE id = attendence_id;
END$$
DELIMITER ;

-- =====================================================
-- CÁC SPs CÒN THIẾU CHO TEACHER REPOSITORY
-- =====================================================

-- tao bai thi
DROP PROCEDURE IF EXISTS `create_exam`;
DELIMITER $$
CREATE PROCEDURE `create_exam`(
    IN exam_name_param VARCHAR(255),
    IN teacher_id_param VARCHAR(255),
    IN course_id_param VARCHAR(255),
    IN exam_date_param VARCHAR(255),
    IN created_date_param VARCHAR(255))
BEGIN
    DECLARE new_exam_id VARCHAR(255);
    SET new_exam_id = CONCAT('exam', SUBSTRING(REPLACE(UUID(), '-', ''), 1, 10));

    INSERT INTO exam (exam_id, exam_name, teacher_id, course_id, exam_date, created_date)
    VALUES (new_exam_id, exam_name_param, teacher_id_param, course_id_param, exam_date_param, created_date_param);
END$$
DELIMITER ;

-- cap nhat bai thi
DROP PROCEDURE IF EXISTS `update_exam`;
DELIMITER $$
CREATE PROCEDURE `update_exam`(
    IN exam_id_param VARCHAR(255),
    IN exam_name_param VARCHAR(255),
    IN exam_date_param VARCHAR(255),
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE exam_owner VARCHAR(255);
    SELECT teacher_id INTO exam_owner
    FROM exam
    WHERE exam_id = exam_id_param;


    IF exam_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bài thi không tồn tại!';
    ELSEIF exam_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền sửa bài thi này!';
    ELSE
        UPDATE exam
        SET
            exam_name = exam_name_param,
            exam_date = exam_date_param
        WHERE exam_id = exam_id_param;
    END IF;
END$$
DELIMITER ;

-- Xoa bai thi
DROP PROCEDURE IF EXISTS `delete_exam`;
DELIMITER $$
CREATE PROCEDURE `delete_exam`(
    IN exam_id_param VARCHAR(255),
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE exam_owner VARCHAR(255);
    SELECT teacher_id INTO exam_owner
    FROM exam
    WHERE exam_id = exam_id_param;

    IF exam_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bài thi không tồn tại!';
    ELSEIF exam_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền sửa bài thi này!';
    ELSE
        DELETE FROM exam WHERE exam_id = exam_id_param;
    END IF;
END$$
DELIMITER ;

-- Cap nhat diem
DROP PROCEDURE IF EXISTS `update_score`;
DELIMITER $$
CREATE PROCEDURE `update_score`(
    IN exam_id_param VARCHAR(255),
    IN student_id_param VARCHAR(255),
    IN score_param INT,
    IN p_teacher_id VARCHAR(255)
)
BEGIN
    DECLARE v_teacher_id VARCHAR(255);
    DECLARE v_course_id VARCHAR(255);
    DECLARE exam_owner VARCHAR(255);

    SELECT teacher_id INTO exam_owner
    FROM exam
    WHERE exam_id = exam_id_param;

    IF exam_owner IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bài thi không tồn tại!';
    ELSEIF exam_owner != p_teacher_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn không có quyền cập nhật điểm cho bài thi này!';
    ELSE
        IF EXISTS (SELECT 1 FROM score WHERE exam_id = exam_id_param AND student_id = student_id_param) THEN
            UPDATE score
            SET score = score_param
            WHERE exam_id = exam_id_param AND student_id = student_id_param;
        ELSE

            SELECT teacher_id, course_id INTO v_teacher_id, v_course_id
            FROM exam
            WHERE exam_id = exam_id_param;

            if v_teacher_id IS NOT NULL THEN
                INSERT INTO score(exam_id, teacher_id, course_id, student_id, score)
                VALUES (exam_id_param, v_teacher_id, v_course_id, student_id_param, score_param);
            END IF;
        END IF;
    END IF;
END$$
DELIMITER ;

-- =====================================================
-- MISSING PROCEDURES FOR StudentController
-- =====================================================

-- Xem thông tin student
DROP PROCEDURE IF EXISTS `show_student_info`;
DELIMITER $$
CREATE PROCEDURE `show_student_info`(IN student_id_param VARCHAR(255))
BEGIN
    SELECT * FROM student WHERE student_id = student_id_param;
END$$
DELIMITER ;

-- Cập nhật thông tin student
DROP PROCEDURE IF EXISTS `update_student`;
DELIMITER $$
CREATE PROCEDURE `update_student`(
    IN student_id_param VARCHAR(255),
    IN student_name_param VARCHAR(255),
    IN user_name_param VARCHAR(255),
    IN user_password_param VARCHAR(255),
    IN birthday_param VARCHAR(255),
    IN gender_param VARCHAR(255))
BEGIN
    UPDATE student
    SET
        student_name = student_name_param,
        user_name = user_name_param,
        user_password = user_password_param,
        birthday = birthday_param,
        gender = gender_param
    WHERE student_id = student_id_param;
END$$
DELIMITER ;

-- Xem các khóa học đã tham gia
DROP PROCEDURE IF EXISTS `show_course_joined`;
DELIMITER $$
CREATE PROCEDURE `show_course_joined`(IN student_id_param VARCHAR(255))
BEGIN
    SELECT
        c.course_id,
        c.course_name,
        c.start_date,
        t.teacher_name,
        a.join_date
    FROM attendance a
             JOIN course c ON a.course_id = c.course_id
             JOIN teacher t ON c.teacher_id = t.teacher_id
    WHERE a.student_id = student_id_param;
END$$
DELIMITER ;

-- Student tham gia khóa học
DROP PROCEDURE IF EXISTS `join_course`;
DELIMITER $$
CREATE PROCEDURE `join_course`(
    IN student_id_param VARCHAR(255),
    IN course_id_param VARCHAR(255),
    IN join_date_param VARCHAR(255))
BEGIN
    DECLARE course_exists INT;
    DECLARE already_joined INT;

    -- Kiểm tra khóa học có tồn tại không
    SELECT COUNT(*) INTO course_exists
    FROM course
    WHERE course_id = course_id_param;

    IF course_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Khóa học không tồn tại!';
    END IF;

    -- Kiểm tra đã tham gia chưa
    SELECT COUNT(*) INTO already_joined
    FROM attendance
    WHERE student_id = student_id_param AND course_id = course_id_param;

    IF already_joined > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn đã tham gia khóa học này!';
    END IF;

    -- Thêm vào attendance
    INSERT INTO attendance(student_id, course_id, join_date)
    VALUES (student_id_param, course_id_param, join_date_param);
END$$
DELIMITER ;

-- Student rời khỏi khóa học
DROP PROCEDURE IF EXISTS `leave_course`;
DELIMITER $$
CREATE PROCEDURE `leave_course`(
    IN student_id_param VARCHAR(255),
    IN course_id_param VARCHAR(255))
BEGIN
    DECLARE attendance_exists INT;

    -- Kiểm tra có tham gia khóa học không
    SELECT COUNT(*) INTO attendance_exists
    FROM attendance
    WHERE student_id = student_id_param AND course_id = course_id_param;

    IF attendance_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bạn chưa tham gia khóa học này!';
    END IF;

    -- Xóa khỏi attendance
    DELETE FROM attendance
    WHERE student_id = student_id_param AND course_id = course_id_param;
END$$
DELIMITER ;

-- diem bai thi trong course
DROP PROCEDURE IF EXISTS `show_student_course_exams_score`;
DELIMITER $$
CREATE PROCEDURE `show_student_course_exams_score`(
    IN student_id_param VARCHAR(255),
    IN course_id_param VARCHAR(255)
)
BEGIN
    SELECT
        e.exam_name AS ExamName,
        e.exam_date AS ExamDate,
        c.course_name AS CourseName,
        t.teacher_name AS TeacherName,
        s.score AS Score
    FROM score s
    JOIN exam e ON s.exam_id = e.exam_id
    JOIN course c ON s.course_id = c.course_id
    JOIN teacher t ON c.teacher_id = t.teacher_id
    WHERE s.student_id = student_id_param AND s.course_id = course_id_param;
END$$
DELIMITER ;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-31 22:37:00