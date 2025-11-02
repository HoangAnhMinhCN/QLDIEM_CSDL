import React, { useEffect, useState } from "react";
import { BookOpen, LogOut, PlusCircle, Search, X, FileText, Calendar, User } from "lucide-react";

// Mock API - Thay thế bằng api từ axiosConfig của bạn
const api = {
    get: async (url) => {
        // Giả lập API response
        await new Promise(resolve => setTimeout(resolve, 500));

        if (url === "/api/student/me/profile") {
            return { data: { studentId: "SV001", studentName: "Nguyễn Văn A", email: "student@example.com" } };
        }
        if (url === "/api/student/me/courses") {
            return { data: [
                    { courseId: "C001", courseName: "Lập trình Java", teacherName: "GV. Trần B", description: "Học lập trình Java cơ bản" }
                ]};
        }
        if (url === "/api/student/courses/available") {
            return { data: [
                    { courseId: "C002", courseName: "Lập trình Python", teacherName: "GV. Lê C", description: "Python cho người mới bắt đầu" }
                ]};
        }
        if (url === "/api/student/courses") {
            return { data: [
                    { courseId: "C001", courseName: "Lập trình Java", teacherName: "GV. Trần B", description: "Học lập trình Java cơ bản" },
                    { courseId: "C002", courseName: "Lập trình Python", teacherName: "GV. Lê C", description: "Python cho người mới bắt đầu" }
                ]};
        }
        if (url.includes("/courses/") && url.includes("/exams")) {
            const courseId = url.split("/")[4];
            return { data: [
                    { examId: "E001", examName: "Kiểm tra giữa kỳ", examDate: "2025-11-15", duration: 60, score: 8.5 }
                ]};
        }
        return { data: [] };
    },
    post: async (url) => {
        await new Promise(resolve => setTimeout(resolve, 500));
        return { data: "Success" };
    },
    delete: async (url) => {
        await new Promise(resolve => setTimeout(resolve, 500));
        return { data: "Success" };
    }
};

export default function Student() {
    const [profile, setProfile] = useState(null);
    const [joinedCourses, setJoinedCourses] = useState([]);
    const [availableCourses, setAvailableCourses] = useState([]);
    const [allCourses, setAllCourses] = useState([]);
    const [searchResults, setSearchResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [searchKeyword, setSearchKeyword] = useState("");
    const [activeTab, setActiveTab] = useState("joined"); // joined, available, all, search
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [courseExams, setCourseExams] = useState([]);
    const [showExamModal, setShowExamModal] = useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [profileRes, joinedRes, availableRes, allRes] = await Promise.all([
                api.get("/api/student/me/profile"),
                api.get("/api/student/me/courses"),
                api.get("/api/student/courses/available"),
                api.get("/api/student/courses")
            ]);

            setProfile(profileRes.data);
            setJoinedCourses(joinedRes.data);
            setAvailableCourses(availableRes.data);
            setAllCourses(allRes.data);
        } catch (err) {
            console.error("Lỗi khi tải dữ liệu:", err);
            if (err.response?.status === 401) {
                alert("Phiên đăng nhập đã hết hạn!");
                handleLogout();
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
            const res = await api.get(`/api/student/courses/search?keyword=${searchKeyword}`);
            setSearchResults(res.data);
            setActiveTab("search");
        } catch (err) {
            alert("Lỗi khi tìm kiếm!");
        }
    };

    const handleViewExams = async (courseId) => {
        try {
            const res = await api.get(`/api/student/courses/${courseId}/exams`);
            setCourseExams(res.data);
            setSelectedCourse(courseId);
            setShowExamModal(true);
        } catch (err) {
            alert("Lỗi khi tải danh sách bài thi!");
        }
    };

    const handleJoin = async (courseId) => {
        try {
            await api.post(`/api/student/courses/${courseId}/join`);
            alert("Đăng ký khóa học thành công!");
            fetchData();
        } catch (err) {
            alert(err.response?.data || "Lỗi khi đăng ký khóa học!");
        }
    };

    const handleLeave = async (courseId) => {
        if (!window.confirm("Bạn có chắc muốn hủy khóa học này?")) return;
        try {
            await api.delete(`/api/student/courses/${courseId}/delete`);
            alert("Hủy khóa học thành công!");
            fetchData();
        } catch (err) {
            alert(err.response?.data || "Lỗi khi hủy khóa học!");
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        window.location.href = "/login";
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-center">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto"></div>
                    <p className="mt-4 text-gray-600">Đang tải dữ liệu...</p>
                </div>
            </div>
        );
    }

    const renderCourseCard = (course, showJoinButton = false, showLeaveButton = false, showExamButton = false) => (
        <div key={course.courseId} className="bg-white rounded-lg shadow-md p-5 hover:shadow-xl transition-shadow">
            <div className="flex items-start justify-between mb-3">
                <h3 className="text-lg font-bold text-blue-600">{course.courseName}</h3>
                <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded">{course.courseId}</span>
            </div>

            <div className="flex items-center gap-2 text-gray-600 text-sm mb-2">
                <User className="w-4 h-4" />
                <span>{course.teacherName}</span>
            </div>

            <p className="text-gray-700 text-sm mb-4 line-clamp-2">{course.description}</p>

            <div className="flex gap-2">
                {showExamButton && (
                    <button
                        onClick={() => handleViewExams(course.courseId)}
                        className="flex-1 flex items-center justify-center gap-2 bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition"
                    >
                        <FileText className="w-4 h-4" /> Xem bài thi
                    </button>
                )}
                {showJoinButton && (
                    <button
                        onClick={() => handleJoin(course.courseId)}
                        className="flex-1 flex items-center justify-center gap-2 bg-green-500 text-white py-2 rounded hover:bg-green-600 transition"
                    >
                        <PlusCircle className="w-4 h-4" /> Tham gia
                    </button>
                )}
                {showLeaveButton && (
                    <button
                        onClick={() => handleLeave(course.courseId)}
                        className="flex-1 bg-red-500 text-white py-2 rounded hover:bg-red-600 transition"
                    >
                        Hủy khóa
                    </button>
                )}
            </div>
        </div>
    );

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
                        <span className="font-medium text-gray-700">{profile?.studentName}</span>
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
                                placeholder="Tìm kiếm khóa học..."
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
                <div className="flex gap-2 mb-6 overflow-x-auto">
                    <button
                        onClick={() => setActiveTab("joined")}
                        className={`px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                            activeTab === "joined" ? "bg-blue-500 text-white" : "bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        📚 Đã tham gia ({joinedCourses.length})
                    </button>
                    <button
                        onClick={() => setActiveTab("available")}
                        className={`px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                            activeTab === "available" ? "bg-blue-500 text-white" : "bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        🆕 Có thể tham gia ({availableCourses.length})
                    </button>
                    <button
                        onClick={() => setActiveTab("all")}
                        className={`px-4 py-2 rounded-lg font-medium transition whitespace-nowrap ${
                            activeTab === "all" ? "bg-blue-500 text-white" : "bg-white text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        🌐 Tất cả khóa học ({allCourses.length})
                    </button>
                    {activeTab === "search" && (
                        <button
                            onClick={() => setActiveTab("joined")}
                            className="px-4 py-2 rounded-lg font-medium bg-purple-500 text-white hover:bg-purple-600 transition whitespace-nowrap"
                        >
                            🔍 Kết quả tìm kiếm ({searchResults.length})
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
                    <div className="text-center py-12">
                        <BookOpen className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                        <p className="text-gray-500 text-lg">Không có khóa học nào</p>
                    </div>
                )}
            </main>

            {/* Exam Modal */}
            {showExamModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
                    <div className="bg-white rounded-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
                        <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                            <h3 className="text-xl font-bold text-gray-800">Danh sách bài thi</h3>
                            <button
                                onClick={() => setShowExamModal(false)}
                                className="text-gray-500 hover:text-gray-700"
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
                                                <h4 className="font-bold text-lg text-blue-600">{exam.examName}</h4>
                                                {exam.score && (
                                                    <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full font-bold">
                                                        Điểm: {exam.score}
                                                    </span>
                                                )}
                                            </div>
                                            <div className="flex items-center gap-4 text-sm text-gray-600">
                                                <div className="flex items-center gap-1">
                                                    <Calendar className="w-4 h-4" />
                                                    <span>{exam.examDate}</span>
                                                </div>
                                                <span>⏱️ {exam.duration} phút</span>
                                            </div>
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
        </div>
    );
}