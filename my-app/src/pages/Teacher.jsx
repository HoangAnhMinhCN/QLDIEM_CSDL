import { useEffect, useState } from "react";
import axios from "axios";

export default function Teacher() {
    const [courses, setCourses] = useState([]);
    const [selectedCourse, setSelectedCourse] = useState(null);
    const [students, setStudents] = useState([]);

    // ⚙️ Lấy danh sách khóa học của giáo viên
    useEffect(() => {
        axios
            .get("/api/teacher/courses", { withCredentials: true })
            .then((res) => setCourses(res.data))
            .catch((err) => console.error("Lỗi lấy khóa học:", err));
    }, []);

    // ⚙️ Lấy danh sách sinh viên trong khóa được chọn
    const loadStudents = (courseId) => {
        setSelectedCourse(courseId);
        axios
            .get(`/api/teacher/courses/${courseId}/students`, { withCredentials: true })
            .then((res) => setStudents(res.data))
            .catch((err) => console.error("Lỗi lấy sinh viên:", err));
    };

    // 🧮 Cập nhật điểm sinh viên (frontend + backend)
    const updateScore = (studentId, newScore) => {
        setStudents((prev) =>
            prev.map((s) => (s.studentId === studentId ? { ...s, score: newScore } : s))
        );

        // Gửi PUT request (ví dụ: cần có endpoint tương ứng trong backend)
        axios
            .put(
                `/api/teacher/courses/${selectedCourse}/students/${studentId}`,
                { score: newScore },
                { withCredentials: true }
            )
            .then(() => console.log("Đã cập nhật điểm"))
            .catch((err) => console.error("Lỗi cập nhật điểm:", err));
    };

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">👩‍🏫 Quản lý khóa học & điểm</h1>

            {/* --- Danh sách khóa học --- */}
            <div className="mb-6">
                <h2 className="text-xl font-semibold mb-2">Khóa học của bạn</h2>
                <div className="flex flex-wrap gap-3">
                    {courses.map((c) => (
                        <button
                            key={c.courseId}
                            onClick={() => loadStudents(c.courseId)}
                            className={`px-4 py-2 rounded-lg border ${
                                selectedCourse === c.courseId
                                    ? "bg-blue-600 text-white"
                                    : "bg-gray-100 hover:bg-gray-200"
                            }`}
                        >
                            {c.courseName}
                        </button>
                    ))}
                </div>
            </div>

            {/* --- Danh sách sinh viên --- */}
            {selectedCourse && (
                <div>
                    <h2 className="text-xl font-semibold mb-2">Sinh viên trong khóa</h2>
                    <table className="w-full border text-center">
                        <thead className="bg-gray-200">
                        <tr>
                            <th className="p-2 border">Mã SV</th>
                            <th className="p-2 border">Tên SV</th>
                            <th className="p-2 border">Điểm</th>
                        </tr>
                        </thead>
                        <tbody>
                        {students.map((s) => (
                            <tr key={s.studentId}>
                                <td className="border p-2">{s.studentId}</td>
                                <td className="border p-2">{s.studentName}</td>
                                <td className="border p-2">
                                    <input
                                        type="number"
                                        value={s.score ?? ""}
                                        onChange={(e) => updateScore(s.studentId, e.target.value)}
                                        className="border p-1 w-16 text-center"
                                    />
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
