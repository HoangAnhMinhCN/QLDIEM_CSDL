import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BookOpen, LogOut, PlusCircle, Search, X, FileText, Calendar, User } from "lucide-react";
import api from "../api/axiosConfig";

export default function Student() {
    const [profile, setProfile] = useState(null);
    const [joinedCourses, setJoinedCourses] = useState([]);
    const [availableCourses, setAvailableCourses] = useState([]);
    const [allCourses, setAllCourses] = useState([]);
    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchKeyword, setSearchKeyword] = useState("");
    const [activeTab, setActiveTab] = useState("joined");
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [courseExams, setCourseExams] = useState([]);
    const [showExamModal, setShowExamModal] = useState(false);

    const navigate = useNavigate();

    // Cấu hình token từ localStorage
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
            console.log("🔄 Đang tải dữ liệu từ database...");

            // Gọi các stored procedures thông qua API endpoints
            const [profileRes, joinedRes, availableRes, allRes] = await Promise.all([
                api.get("/api/student/me/profile"),           // CALL show_student_info
                api.get("/api/student/me/courses"),           // CALL show_course_joined
                api.get("/api/student/courses/available"),    // CALL find_available_courses_for_student
                api.get("/api/student/courses")               // CALL get_all_course
            ]);

            console.log("✅ Profile:", profileRes.data);
            console.log("✅ Joined courses:", joinedRes.data);
            console.log("✅ Available courses:", availableRes.data);
            console.log("✅ All courses:", allRes.data);

            setProfile(profileRes.data);
            setJoinedCourses(joinedRes.data);
            setAvailableCourses(availableRes.data);
            setAllCourses(allRes.data);
        } catch (err) {
            console.error("❌ Lỗi khi tải dữ liệu:", err);
            if (err.response?.status === 401) {
                alert("Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại.");
                handleLogout();
            } else {
                alert("Lỗi khi tải dữ liệu: " + (err.response?.data || err.message));
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async () => {
        if (!searchKeyword.trim()) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }
        try {
            console.log("🔍 Tìm kiếm:", searchKeyword);
            // CALL search_course
            const res = await api.get(`/api/student/courses/search?keyword=${searchKeyword}`);
            console.log("✅ Kết quả tìm kiếm:", res.data);
            setSearchResults(res.data);
            setActiveTab("search");
        } catch (err) {
            console.error("❌ Lỗi tìm kiếm:", err);
            alert("Lỗi khi tìm kiếm: " + (err.response?.data || err.message));
        }
    };

    const handleViewExams = async (courseId) => {
        try {
            console.log("📝 Xem bài thi của khóa:", courseId);
            // CALL show_student_exams_in_course
            const res = await api.get(`/api/student/courses/${courseId}/exams`);
            console.log("✅ Danh sách bài thi:", res.data);
            setCourseExams(res.data);
            setSelectedCourse(courseId);
            setShowExamModal(true);
        } catch (err) {
            console.error("❌ Lỗi khi tải bài thi:", err);
            alert("Lỗi khi tải danh sách bài thi: " + (err.response?.data || err.message));
        }
    };

    const handleJoin = async (courseId) => {
        if (!courseId) {
            alert("Lỗi: Course ID không hợp lệ!");
            console.error("❌ courseId is null or undefined");
            return;
        }

        try {
            console.log("➕ Đăng ký khóa học:");
            console.log("   - Course ID:", courseId);
            console.log("   - API URL:", `/api/student/courses/${courseId}/join`);

            // CALL join_course
            const response = await api.post(`/api/student/courses/${courseId}/join`);
            console.log("✅ Response:", response);

            alert("Đăng ký khóa học thành công!");
            fetchData(); // Reload lại dữ liệu
        } catch (err) {
            console.error("❌ Lỗi đăng ký:");
            console.error("   - Status:", err.response?.status);
            console.error("   - Data:", err.response?.data);
            console.error("   - Full error:", err);

            alert(err.response?.data || "Lỗi khi đăng ký khóa học!");
        }
    };

    const handleLeave = async (courseId) => {
        if (!window.confirm("Bạn có chắc muốn hủy khóa học này?")) return;

        try {
            console.log("➖ Hủy khóa học:", courseId);
            // CALL leave_course
            await api.delete(`/api/student/courses/${courseId}/delete`);
            alert("Hủy khóa học thành công!");
            console.log("✅ Hủy thành công, reload dữ liệu...");
            fetchData(); // Reload lại dữ liệu
        } catch (err) {
            console.error("❌ Lỗi hủy khóa:", err);
            alert(err.response?.data || "Lỗi khi hủy khóa học!");
        }
    };

    const handleLogout = () => {
        console.log("👋 Đăng xuất...");
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        navigate("/login");
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-gray-50">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                    <p className="mt-4 text-gray-600">Đang tải dữ liệu từ database...</p>
                </div>
            </div>
        );
    }

    const renderCourseCard = (course, showJoinButton = false, showLeaveButton = false, showExamButton = false) => {
        // Xử lý tên field linh hoạt - backend có thể trả về nhiều format khác nhau
        const courseId = course.courseId || course.courseID || course.course_id;
        const courseName = course.courseName || course.course_name;
        const teacherName = course.teacherName || course.teacher_name;
        const startDate = course.startDate || course.start_date;
        const joinDate = course.joinDate || course.join_date;

        console.log("🎯 Rendering course:", {
            courseId,
            courseName,
            teacherName,
            startDate,
            joinDate,
            raw: course
        });

        return (
            <div key={courseId} className="bg-white rounded-lg shadow-md p-5 hover:shadow-xl transition-shadow">
                <div className="flex items-start justify-between mb-3">
                    <h3 className="text-lg font-bold text-blue-600">
                        {courseName}
                    </h3>
                    <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">
                        {courseId}
                    </span>
                </div>

                <div className="flex items-center gap-2 text-gray-600 text-sm mb-2">
                    <User className="w-4 h-4" />
                    <span>GV. {teacherName}</span>
                </div>

                {startDate && (
                    <p className="text-gray-500 text-xs mb-2">
                        📅 Ngày bắt đầu: {startDate}
                    </p>
                )}

                {course.joinDate && (
                    <p className="text-gray-500 text-xs mb-2">
                        ✅ Đã tham gia: {joinDate}
                    </p>
                )}

                <div className="flex gap-2 mt-4">
                    {showExamButton && (
                        <button
                            onClick={() => handleViewExams(courseId)}
                            className="flex-1 flex items-center justify-center gap-2 bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition text-sm"
                        >
                            <FileText className="w-4 h-4" /> Xem bài thi
                        </button>
                    )}
                    {showJoinButton && (
                        <button
                            onClick={() => {
                                console.log("🔵 Attempting to join course:", courseId);
                                handleJoin(courseId);
                            }}
                            className="flex-1 flex items-center justify-center gap-2 bg-green-500 text-white py-2 rounded hover:bg-green-600 transition text-sm"
                        >
                            <PlusCircle className="w-4 h-4" /> Tham gia
                        </button>
                    )}
                    {showLeaveButton && (
                        <button
                            onClick={() => {
                                console.log("🔴 Attempting to leave course:", courseId);
                                handleLeave(courseId);
                            }}
                            className="flex-1 bg-red-500 text-white py-2 rounded hover:bg-red-600 transition text-sm"
                        >
                            Hủy khóa
                        </button>
                    )}
                </div>
            </div>
        );
    };

    return (
        <div className="min-h-screen bg-gray-50">
            {/* Header */}
            <header className="bg-white shadow-md sticky top-0 z-10">
                <div className="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
                    <h1 className="text-2xl font-bold text-blue-600 flex items-center gap-2">
                        <BookOpen className="w-7 h-7" />
                        Học sinh
                    </h1>
                    <div className="flex items-center gap-4">
                        <span className="font-medium text-gray-700">
                            {profile?.studentName || "Học sinh"}
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
                {/* Search Bar */}
                <div className="bg-white rounded-lg shadow-md p-4 mb-6">
                    <div className="flex gap-2">
                        <div className="flex-1 relative">
                            <Search className="absolute left-3 top-3 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                placeholder="Tìm kiếm khóa học theo tên..."
                                value={searchKeyword}
                                onChange={(e) => setSearchKeyword(e.target.value)}
                                onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>
                        <button
                            onClick={handleSearch}
                            className="bg-blue-500 text-white px-6 py-2 rounded-lg hover:bg-blue-600 transition"
                        >
                            Tìm kiếm
                        </button>
                    </div>
                </div>

                {/* Tabs */}
                <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
                    <button
                        onClick={() => setActiveTab("joined")}
                        className={`px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                            activeTab === "joined" ? "bg-blue-500 text-white shadow-lg" : "bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        📚 Đã tham gia ({joinedCourses.length})
                    </button>
                    <button
                        onClick={() => setActiveTab("available")}
                        className={`px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                            activeTab === "available" ? "bg-blue-500 text-white shadow-lg" : "bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        🆕 Có thể tham gia ({availableCourses.length})
                    </button>
                    <button
                        onClick={() => setActiveTab("all")}
                        className={`px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                            activeTab === "all" ? "bg-blue-500 text-white shadow-lg" : "bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        🌐 Tất cả khóa học ({allCourses.length})
                    </button>
                    {activeTab === "search" && searchResults.length > 0 && (
                        <button
                            className="px-4 py-2 rounded-lg font-medium bg-purple-500 text-white shadow-lg transition whitespace-nowrap"
                        >
                            🔍 Kết quả: "{searchKeyword}" ({searchResults.length})
                        </button>
                    )}
                </div>

                {/* Course Grid */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
                    {activeTab === "joined" && joinedCourses.length > 0 &&
                        joinedCourses.map(course => renderCourseCard(course, false, true, true))}

                    {activeTab === "available" && availableCourses.length > 0 &&
                        availableCourses.map(course => renderCourseCard(course, true, false, false))}

                    {activeTab === "all" && allCourses.length > 0 &&
                        allCourses.map(course => renderCourseCard(course, false, false, false))}

                    {activeTab === "search" && searchResults.length > 0 &&
                        searchResults.map(course => renderCourseCard(course, false, false, false))}
                </div>

                {/* Empty State */}
                {((activeTab === "joined" && joinedCourses.length === 0) ||
                    (activeTab === "available" && availableCourses.length === 0) ||
                    (activeTab === "all" && allCourses.length === 0) ||
                    (activeTab === "search" && searchResults.length === 0)) && (
                    <div className="text-center py-12 bg-white rounded-lg">
                        <BookOpen className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-500 text-lg">
                            {activeTab === "search"
                                ? `Không tìm thấy khóa học nào với từ khóa "${searchKeyword}"`
                                : activeTab === "joined"
                                    ? "Bạn chưa tham gia khóa học nào"
                                    : activeTab === "available"
                                        ? "Hiện không có khóa học nào để tham gia"
                                        : "Không có khóa học nào"}
                        </p>
                    </div>
                )}
            </main>

            {/* Exam Modal */}
            {showExamModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
                        <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                            <h3 className="text-xl font-bold text-gray-800">
                                📝 Danh sách bài thi
                            </h3>
                            <button
                                onClick={() => setShowExamModal(false)}
                                className="text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-full p-1 transition"
                            >
                                <X className="w-6 h-6" />
                            </button>
                        </div>

                        <div className="p-6">
                            {courseExams.length > 0 ? (
                                <div className="space-y-4">
                                    {courseExams.map((exam) => (
                                        <div key={exam.examId} className="border rounded-lg p-4 hover:shadow-md transition">
                                            <div className="flex justify-between items-start mb-2">
                                                <h4 className="font-bold text-lg text-blue-600">
                                                    {exam.examName}
                                                </h4>
                                                {exam.score !== null && exam.score !== undefined && (
                                                    <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full font-bold">
                                                        Điểm: {exam.score}
                                                    </span>
                                                )}
                                            </div>
                                            <div className="space-y-1 text-sm text-gray-600">
                                                {exam.courseName && (
                                                    <p>📚 Khóa học: {exam.courseName}</p>
                                                )}
                                                {exam.teacherName && (
                                                    <p>👨‍🏫 Giảng viên: {exam.teacherName}</p>
                                                )}
                                                {exam.examDate && (
                                                    <div className="flex items-center gap-1 mt-2">
                                                        <Calendar className="w-4 h-4" />
                                                        <span>Ngày thi: {exam.examDate}</span>
                                                    </div>
                                                )}
                                                {exam.createdDate && (
                                                    <p className="text-xs text-gray-400">
                                                        Ngày tạo: {exam.createdDate}
                                                    </p>
                                                )}
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <div className="text-center py-8">
                                    <FileText className="w-12 h-12 text-gray-300 mx-auto mb-3" />
                                    <p className="text-gray-500">Chưa có bài thi nào trong khóa học này</p>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}