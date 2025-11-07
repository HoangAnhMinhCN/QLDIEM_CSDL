import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BookOpen, LogOut, PlusCircle, X, FileText, Calendar, User, Edit, Trash2, Users } from "lucide-react";
import api from "../api/axiosConfig";

export default function Teacher() {
    const [profile, setProfile] = useState(null);
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [students, setStudents] = useState([]);
    const [exams, setExams] = useState([]);
    const [loading, setLoading] = useState(true);

    // Modals
    const [showCreateCourseModal, setShowCreateCourseModal] = useState(false);
    const [showEditCourseModal, setShowEditCourseModal] = useState(false);
    const [showStudentsModal, setShowStudentsModal] = useState(false);
    const [showExamsModal, setShowExamsModal] = useState(false);
    const [showCreateExamModal, setShowCreateExamModal] = useState(false);
    const [showEditExamModal, setShowEditExamModal] = useState(false);
    const [showScoresModal, setShowScoresModal] = useState(false);

    // Form data
    const [courseForm, setCourseForm] = useState({ courseName: "", startDate: "" });
    const [examForm, setExamForm] = useState({ examName: "", examDate: "" });
    const [editingExam, setEditingExam] = useState(null);
    const [selectedExamScores, setSelectedExamScores] = useState([]);
    const [editedScores, setEditedScores] = useState({});

    const navigate = useNavigate();
    const token = localStorage.getItem("token");

    useEffect(() => {
        if (token) {
            api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
            fetchData();
        } else {
            alert("Vui lòng đăng nhập!");
            navigate("/login");
        }
    }, []);

    const fetchData = async () => {
        try {
            console.log("🔄 Đang tải dữ liệu giáo viên...");

            const [profileRes, coursesRes] = await Promise.all([
                api.get("/api/teacher/profile"),      // CALL show_teacher_info
                api.get("/api/teacher/courses")       // CALL show_course_teached
            ]);

            console.log("✅ Teacher Profile:", profileRes.data);
            console.log("✅ Teacher Courses:", coursesRes.data);

            setProfile(profileRes.data);
            setCourses(coursesRes.data);
        } catch (err) {
            console.error("❌ Lỗi khi tải dữ liệu:", err);
            if (err.response?.status === 401) {
                alert("Phiên đăng nhập đã hết hạn!");
                handleLogout();
            }
        } finally {
            setLoading(false);
        }
    };

    // ==================== COURSE MANAGEMENT ====================

    const handleCreateCourse = async (e) => {
        e.preventDefault();
        try {
            console.log("➕ Tạo khóa học:", courseForm);
            await api.post("/api/teacher/courses", courseForm); // CALL create_course
            alert("Tạo khóa học thành công!");
            setShowCreateCourseModal(false);
            setCourseForm({ courseName: "", startDate: "" });
            fetchData();
        } catch (err) {
            console.error("❌ Lỗi tạo khóa học:", err);
            alert(err.response?.data || "Lỗi khi tạo khóa học!");
        }
    };

    const handleEditCourse = async (e) => {
        e.preventDefault();
        try {
            console.log("✏️ Sửa khóa học:", selectedCourse.courseId, courseForm);
            await api.put(`/api/teacher/courses/${selectedCourse.courseId}`, courseForm); // CALL update_course
            alert("Cập nhật khóa học thành công!");
            setShowEditCourseModal(false);
            fetchData();
        } catch (err) {
            console.error("❌ Lỗi sửa khóa học:", err);
            alert(err.response?.data || "Lỗi khi cập nhật khóa học!");
        }
    };

    const handleDeleteCourse = async (courseId) => {
        if (!window.confirm("Bạn có chắc muốn xóa khóa học này? Tất cả dữ liệu liên quan sẽ bị xóa!")) return;

        try {
            console.log("🗑️ Xóa khóa học:", courseId);
            await api.delete(`/api/teacher/courses/${courseId}`); // CALL delete_course
            alert("Xóa khóa học thành công!");
            fetchData();
        } catch (err) {
            console.error("❌ Lỗi xóa khóa học:", err);
            alert(err.response?.data || "Lỗi khi xóa khóa học!");
        }
    };

    // ==================== STUDENT MANAGEMENT ====================

    const handleViewStudents = async (course) => {
        try {
            console.log("👥 Xem danh sách sinh viên:", course.courseId);
            const res = await api.get(`/api/teacher/courses/${course.courseId}/students`); // CALL show_course_studentList
            console.log("✅ Students:", res.data);
            setStudents(res.data);
            setSelectedCourse(course);
            setShowStudentsModal(true);
        } catch (err) {
            console.error("❌ Lỗi tải danh sách sinh viên:", err);
            alert("Lỗi khi tải danh sách sinh viên!");
        }
    };

    const handleRemoveStudent = async (studentId) => {
        if (!window.confirm("Bạn có chắc muốn xóa sinh viên này khỏi khóa học?")) return;

        try {
            console.log("➖ Xóa sinh viên:", studentId);
            await api.delete(`/api/teacher/courses/${selectedCourse.courseId}/students/${studentId}`); // CALL remove_student_from_course
            alert("Đã xóa sinh viên khỏi khóa học!");
            handleViewStudents(selectedCourse); // Reload
        } catch (err) {
            console.error("❌ Lỗi xóa sinh viên:", err);
            alert(err.response?.data || "Lỗi khi xóa sinh viên!");
        }
    };

    // ==================== EXAM MANAGEMENT ====================

    const handleViewExams = async (course) => {
        try {
            console.log("📝 Xem danh sách bài thi:", course.courseId);
            const res = await api.get(`/api/teacher/courses/${course.courseId}/exams`); // CALL show_course_exams
            console.log("✅ Exams:", res.data);
            setExams(res.data);
            setSelectedCourse(course);
            setShowExamsModal(true);
        } catch (err) {
            console.error("❌ Lỗi tải danh sách bài thi:", err);
            alert("Lỗi khi tải danh sách bài thi!");
        }
    };

    const handleCreateExam = async (e) => {
        e.preventDefault();
        try {
            console.log("➕ Tạo bài thi:", examForm);
            await api.post(`/api/teacher/courses/${selectedCourse.courseId}/exams`, examForm); // CALL create_exam
            alert("Tạo bài thi thành công!");
            setShowCreateExamModal(false);
            setExamForm({ examName: "", examDate: "" });
            handleViewExams(selectedCourse); // Reload
        } catch (err) {
            console.error("❌ Lỗi tạo bài thi:", err);
            alert(err.response?.data || "Lỗi khi tạo bài thi!");
        }
    };

    const handleEditExam = async (e) => {
        e.preventDefault();
        try {
            console.log("✏️ Sửa bài thi:", editingExam.examId, examForm);
            await api.put(`/api/teacher/exams/${editingExam.examId}`, examForm); // CALL update_exam
            alert("Cập nhật bài thi thành công!");
            setShowEditExamModal(false);
            setEditingExam(null);
            handleViewExams(selectedCourse); // Reload
        } catch (err) {
            console.error("❌ Lỗi sửa bài thi:", err);
            alert(err.response?.data || "Lỗi khi cập nhật bài thi!");
        }
    };

    const handleDeleteExam = async (examId) => {
        if (!window.confirm("Bạn có chắc muốn xóa bài thi này?")) return;

        try {
            console.log("🗑️ Xóa bài thi:", examId);
            await api.delete(`/api/teacher/exams/${examId}`); // CALL delete_exam
            alert("Xóa bài thi thành công!");
            handleViewExams(selectedCourse); // Reload
        } catch (err) {
            console.error("❌ Lỗi xóa bài thi:", err);
            alert(err.response?.data || "Lỗi khi xóa bài thi!");
        }
    };

    const handleViewScores = async (examId, examName) => {
        try {
            console.log("📊 Xem điểm bài thi:", examId);
            const res = await api.get(`/api/teacher/exams/${examId}/scores`); // CALL show_exam_scores
            console.log("✅ Scores:", res.data);
            setSelectedExamScores(res.data);
            setEditingExam({ examId, examName }); // Store exam info for modal title
            setShowScoresModal(true);
            
            // Initialize editedScores with current scores
            const initialScores = {};
            res.data.forEach(score => {
                initialScores[score.studentId] = score.score;
            });
            setEditedScores(initialScores);
        } catch (err) {
            console.error("❌ Lỗi tải điểm thi:", err);
            alert("Lỗi khi tải điểm thi!");
        }
    };

    const handleScoreChange = (studentId, newScore) => {
        // Don't allow invalid values
        if (newScore === '' || (parseFloat(newScore) >= 0 && parseFloat(newScore) <= 10)) {
            setEditedScores(prev => ({
                ...prev,
                [studentId]: newScore
            }));
        }
    };

    const handleSaveScores = async () => {
        try {
            // Validate all scores
            const invalidScores = Object.entries(editedScores).filter(([_, score]) => {
                const numScore = parseFloat(score);
                return isNaN(numScore) || numScore < 0 || numScore > 10;
            });

            if (invalidScores.length > 0) {
                alert("Điểm không hợp lệ! Điểm phải từ 0 đến 10.");
                return;
            }

            // Filter out scores that haven't changed
            const changedScores = Object.entries(editedScores).filter(([studentId, newScore]) => {
                const originalScore = selectedExamScores.find(s => s.studentId === studentId)?.score;
                return newScore !== originalScore;
            });

            if (changedScores.length === 0) {
                alert("Không có thay đổi nào để lưu!");
                return;
            }

            console.log("💾 Lưu điểm thi:", changedScores);

            // Update scores sequentially
            for (const [studentId, score] of changedScores) {
                await api.put(`/api/teacher/exams/${editingExam.examId}/scores/${studentId}`, { score: parseFloat(score) }); // CALL update_score
            }

            alert("Cập nhật điểm thành công!");
            
            // Reload scores
            const res = await api.get(`/api/teacher/exams/${editingExam.examId}/scores`);
            setSelectedExamScores(res.data);

            // Reset edited scores
            const initialScores = {};
            res.data.forEach(score => {
                initialScores[score.studentId] = score.score;
            });
            setEditedScores(initialScores);
        } catch (err) {
            console.error("❌ Lỗi cập nhật điểm:", err);
            alert(err.response?.data || "Lỗi khi cập nhật điểm!");
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        navigate("/login");
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                    <p className="mt-4 text-gray-600">Đang tải dữ liệu...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-md sticky top-0 z-10 w-screen">
                <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
                    <h1 className="text-2xl font-bold text-blue-600 flex items-center gap-2">
                        <BookOpen className="w-7 h-7" />
                        Giáo viên
                    </h1>
                    <div className="flex items-center gap-4">
                        <span className="font-medium text-gray-700">
                            {profile?.teacherName || "Giáo viên"}
                        </span>
                        <button
                            onClick={handleLogout}
                            className="flex items-center gap-2 bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition"
                        >
                            <LogOut className="w-4 h-4" /> Đăng xuất
                        </button>
                    </div>
                </div>
            </header>

            <main className="max-w-7xl mx-auto px-4 py-6">
                {/* Header Actions */}
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-xl font-bold text-gray-800">📚 Các khóa học của tôi ({courses.length})</h2>
                    <button
                        onClick={() => setShowCreateCourseModal(true)}
                        className="flex items-center gap-2 bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition"
                    >
                        <PlusCircle className="w-5 h-5" /> Tạo khóa học mới
                    </button>
                </div>

                {/* Courses Grid */}
                {courses.length > 0 ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                        {courses.map((course) => (
                            <div key={course.courseId} className="bg-white rounded-lg shadow-md p-5 hover:shadow-xl transition">
                                <div className="flex justify-between items-start mb-3">
                                    <div>
                                        <h3 className="text-lg font-bold text-blue-600">{course.courseName}</h3>
                                        <p className="text-xs text-gray-500 mt-1">{course.courseId}</p>
                                    </div>
                                    <div className="flex gap-1">
                                        <button
                                            onClick={() => {
                                                setSelectedCourse(course);
                                                setCourseForm({ courseName: course.courseName, startDate: course.startDate || "" });
                                                setShowEditCourseModal(true);
                                            }}
                                            className="p-1 text-blue-500 hover:bg-blue-50 rounded"
                                        >
                                            <Edit className="w-4 h-4" />
                                        </button>
                                        <button
                                            onClick={() => handleDeleteCourse(course.courseId)}
                                            className="p-1 text-red-500 hover:bg-red-50 rounded"
                                        >
                                            <Trash2 className="w-4 h-4" />
                                        </button>
                                    </div>
                                </div>

                                {course.startDate && (
                                    <p className="text-xs text-gray-500 mb-3 flex items-center gap-1">
                                        <Calendar className="w-3 h-3" /> {course.startDate}
                                    </p>
                                )}

                                <div className="flex gap-2">
                                    <button
                                        onClick={() => handleViewStudents(course)}
                                        className="flex-1 flex items-center justify-center gap-1 bg-green-500 text-white py-2 rounded text-sm hover:bg-green-600 transition"
                                    >
                                        <Users className="w-4 h-4" /> Sinh viên
                                    </button>
                                    <button
                                        onClick={() => handleViewExams(course)}
                                        className="flex-1 flex items-center justify-center gap-1 bg-purple-500 text-white py-2 rounded text-sm hover:bg-purple-600 transition"
                                    >
                                        <FileText className="w-4 h-4" /> Bài thi
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                ) : (
                    <div className="text-center py-12 bg-white rounded-lg">
                        <BookOpen className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-500">Bạn chưa có khóa học nào</p>
                    </div>
                )}
            </main>

            {/* Create Course Modal */}
            {showCreateCourseModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-md w-full p-6">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-bold">Tạo khóa học mới</h3>
                            <button onClick={() => setShowCreateCourseModal(false)} className="text-gray-500 hover:text-gray-700">
                                <X className="w-6 h-6" />
                            </button>
                        </div>
                        <form onSubmit={handleCreateCourse}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Tên khóa học *</label>
                                <input
                                    type="text"
                                    required
                                    value={courseForm.courseName}
                                    onChange={(e) => setCourseForm({...courseForm, courseName: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Ngày bắt đầu</label>
                                <input
                                    type="date"
                                    value={courseForm.startDate}
                                    onChange={(e) => setCourseForm({...courseForm, startDate: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600">
                                Tạo khóa học
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Edit Course Modal */}
            {showEditCourseModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-md w-full p-6">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-bold">Sửa khóa học</h3>
                            <button onClick={() => setShowEditCourseModal(false)} className="text-gray-500 hover:text-gray-700">
                                <X className="w-6 h-6" />
                            </button>
                        </div>
                        <form onSubmit={handleEditCourse}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Tên khóa học *</label>
                                <input
                                    type="text"
                                    required
                                    value={courseForm.courseName}
                                    onChange={(e) => setCourseForm({...courseForm, courseName: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Ngày bắt đầu</label>
                                <input
                                    type="date"
                                    value={courseForm.startDate}
                                    onChange={(e) => setCourseForm({...courseForm, startDate: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600">
                                Cập nhật
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Students Modal */}
            {showStudentsModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
                        <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                            <h3 className="text-xl font-bold">👥 Sinh viên - {selectedCourse?.courseName}</h3>
                            <button onClick={() => setShowStudentsModal(false)} className="text-gray-500 hover:text-gray-700">
                                <X className="w-6 h-6" />
                            </button>
                        </div>
                        <div className="p-6">
                            {students.length > 0 ? (
                                <div className="space-y-3">
                                    {students.map((student) => (
                                        <div key={student.studentId} className="flex justify-between items-center border rounded-lg p-3">
                                            <div>
                                                <p className="font-semibold">{student.studentName}</p>
                                                <p className="text-xs text-gray-500">{student.studentId}</p>
                                            </div>
                                            <button
                                                onClick={() => handleRemoveStudent(student.studentId)}
                                                className="text-red-500 hover:bg-red-50 p-2 rounded"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-center text-gray-500 py-8">Chưa có sinh viên nào</p>
                            )}
                        </div>
                    </div>
                </div>
            )}

            {/* Exams Modal */}
            {showExamsModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
                        <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                            <h3 className="text-xl font-bold">📝 Bài thi - {selectedCourse?.courseName}</h3>
                            <div className="flex gap-2">
                                <button
                                    onClick={() => setShowCreateExamModal(true)}
                                    className="bg-blue-500 text-white px-3 py-1 rounded text-sm hover:bg-blue-600"
                                >
                                    + Tạo bài thi
                                </button>
                                <button onClick={() => setShowExamsModal(false)} className="text-gray-500 hover:text-gray-700">
                                    <X className="w-6 h-6" />
                                </button>
                            </div>
                        </div>
                        <div className="p-6">
                            {exams.length > 0 ? (
                                <div className="space-y-3">
                                    {exams.map((exam) => (
                                        <div 
                                            key={exam.examId} 
                                            className="border rounded-lg p-4 cursor-pointer hover:bg-gray-50 transition"
                                            onClick={() => handleViewScores(exam.examId, exam.examName)}
                                        >
                                            <div className="flex justify-between items-start mb-2">
                                                <div>
                                                    <h4 className="font-bold text-blue-600">{exam.examName}</h4>
                                                    <p className="text-xs text-gray-500">{exam.examId}</p>
                                                </div>
                                                <div className="flex gap-1">
                                                    <button
                                                        onClick={(e) => {
                                                            e.stopPropagation(); // Prevent opening scores modal
                                                            setEditingExam(exam);
                                                            setExamForm({ examName: exam.examName, examDate: exam.examDate || "" });
                                                            setShowEditExamModal(true);
                                                        }}
                                                        className="p-1 text-blue-500 hover:bg-blue-50 rounded"
                                                    >
                                                        <Edit className="w-4 h-4" />
                                                    </button>
                                                    <button
                                                        onClick={(e) => {
                                                            e.stopPropagation(); // Prevent opening scores modal
                                                            handleDeleteExam(exam.examId);
                                                        }}
                                                        className="p-1 text-red-500 hover:bg-red-50 rounded"
                                                    >
                                                        <Trash2 className="w-4 h-4" />
                                                    </button>
                                                </div>
                                            </div>
                                            {exam.examDate && (
                                                <p className="text-sm text-gray-600 flex items-center gap-1">
                                                    <Calendar className="w-4 h-4" /> {exam.examDate}
                                                </p>
                                            )}
                                            {exam.createdDate && (
                                                <p className="text-xs text-gray-400 mt-1">Tạo: {exam.createdDate}</p>
                                            )}
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="text-center text-gray-500 py-8">Chưa có bài thi nào</p>
                            )}
                        </div>
                    </div>
                </div>
            )}

            {/* Create Exam Modal */}
            {showCreateExamModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-md w-full p-6">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-bold">Tạo bài thi mới</h3>
                            <button onClick={() => setShowCreateExamModal(false)} className="text-gray-500 hover:text-gray-700">
                                <X className="w-6 h-6" />
                            </button>
                        </div>
                        <form onSubmit={handleCreateExam}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Tên bài thi *</label>
                                <input
                                    type="text"
                                    required
                                    value={examForm.examName}
                                    onChange={(e) => setExamForm({...examForm, examName: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Ngày thi</label>
                                <input
                                    type="date"
                                    value={examForm.examDate}
                                    onChange={(e) => setExamForm({...examForm, examDate: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600">
                                Tạo bài thi
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Edit Exam Modal */}
            {showEditExamModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-md w-full p-6">
                        <div className="flex justify-between items-center mb-4">
                            <h3 className="text-xl font-bold">Sửa bài thi</h3>
                            <button onClick={() => setShowEditExamModal(false)} className="text-gray-500 hover:text-gray-700">
                                <X className="w-6 h-6" />
                            </button>
                        </div>
                        <form onSubmit={handleEditExam}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Tên bài thi *</label>
                                <input
                                    type="text"
                                    required
                                    value={examForm.examName}
                                    onChange={(e) => setExamForm({...examForm, examName: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium mb-2">Ngày thi</label>
                                <input
                                    type="date"
                                    value={examForm.examDate}
                                    onChange={(e) => setExamForm({...examForm, examDate: e.target.value})}
                                    className="w-full border rounded-lg px-3 py-2 focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <button type="submit" className="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600">
                                Cập nhật
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Exam Scores Modal */}
            {showScoresModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
                        <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                            <h3 className="text-xl font-bold">📊 Điểm bài thi - {editingExam?.examName}</h3>
                            <button onClick={() => setShowScoresModal(false)} className="text-gray-500 hover:text-gray-700">
                                <X className="w-6 h-6" />
                            </button>
                        </div>
                        <div className="p-6">
                            {selectedExamScores.length > 0 ? (
                                <>
                                    <div className="relative overflow-x-auto mb-4">
                                        <table className="w-full text-left text-gray-600">
                                            <thead className="text-xs uppercase bg-gray-50">
                                                <tr>
                                                    <th className="px-6 py-3">Mã SV</th>
                                                    <th className="px-6 py-3">Họ tên</th>
                                                    <th className="px-6 py-3">Điểm</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                {selectedExamScores.map((score, index) => (
                                                    <tr key={score.studentId} className={index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}>
                                                        <td className="px-6 py-4">{score.studentId}</td>
                                                        <td className="px-6 py-4">{score.studentName}</td>
                                                        <td className="px-6 py-4">
                                                            <input
                                                                type="number"
                                                                min="0"
                                                                max="10"
                                                                step="0.1"
                                                                value={editedScores[score.studentId] || ''}
                                                                onChange={(e) => handleScoreChange(score.studentId, e.target.value)}
                                                                className="w-20 border rounded px-2 py-1 focus:ring-2 focus:ring-blue-500"
                                                            />
                                                        </td>
                                                    </tr>
                                                ))}
                                            </tbody>
                                        </table>
                                    </div>
                                    <div className="flex justify-end">
                                        <button
                                            onClick={handleSaveScores}
                                            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition flex items-center gap-2"
                                        >
                                            💾 Lưu thay đổi
                                        </button>
                                    </div>
                                </>
                            ) : (
                                <p className="text-center text-gray-500 py-8">Chưa có sinh viên nào có điểm</p>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}