import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { BookOpen, User, Lock, LogIn } from "lucide-react";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            // Xóa token cũ
            localStorage.clear();

            // Gọi API đăng nhập
            const response = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    username,
                    password,
                }),
            });

            if (!response.ok) {
                throw new Error("Đăng nhập thất bại!");
            }

            const data = await response.json();

            const token = data.accessToken;
            const roles = data.roles || data.authorities || [];

            // Lưu vào localStorage
            localStorage.setItem("token", token);
            localStorage.setItem("user", JSON.stringify(data));

            // Kiểm tra vai trò
            let isTeacher = false;
            let isStudent = false;

            roles.forEach((role) => {
                const roleName = typeof role === 'string' ? role : (role.authority || role.role);

                if (roleName === "ROLE_TEACHER" || roleName === "TEACHER") {
                    isTeacher = true;
                }
                if (roleName === "ROLE_STUDENT" || roleName === "STUDENT") {
                    isStudent = true;
                }
            });

            // Điều hướng theo vai trò
            if (isStudent) {
                alert("Đăng nhập thành công! Chào học sinh 👨‍🎓");
                setTimeout(() => navigate("/student", { replace: true }), 300);
            } else if (isTeacher) {
                alert("Đăng nhập thành công! Chào giáo viên 👩‍🏫");
                setTimeout(() => navigate("/teacher", { replace: true }), 300);
            } else {
                // Fallback: dựa vào userId
                if (data.userId && data.userId.toLowerCase().includes("teacher")) {
                    alert("Đăng nhập thành công! Chào giáo viên 👩‍🏫");
                    setTimeout(() => navigate("/teacher", { replace: true }), 300);
                } else {
                    alert("Đăng nhập thành công! Chào học sinh 👨‍🎓");
                    setTimeout(() => navigate("/student", { replace: true }), 300);
                }
            }
        } catch (err) {
            console.error("Login error:", err);
            alert("Đăng nhập thất bại! Vui lòng kiểm tra tên đăng nhập và mật khẩu.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page min-h-screen w-screen fixed inset-0 grid place-items-center bg-gradient-to-br from-blue-50 via-white to-blue-50 p-4">
            <div className="w-full max-w-md">
                {/* Logo & Title */}
                <div className="text-center mb-6">
                    <div className="inline-flex items-center justify-center w-16 h-16 bg-blue-600 rounded-full mb-3">
                        <BookOpen className="w-8 h-8 text-white" />
                    </div>
                    <h1 className="text-2xl font-bold text-gray-800 mb-1">
                        Hệ thống quản lý điểm
                    </h1>
                    <p className="text-sm text-gray-600">
                        Đăng nhập để tiếp tục
                    </p>
                </div>

                {/* Login Form */}
                <form
                    onSubmit={handleLogin}
                    className="bg-white p-8 rounded-2xl shadow-lg border border-gray-100"
                >
                    <h2 className="text-xl font-bold mb-6 text-center text-gray-800">
                        Đăng nhập
                    </h2>

                    {/* Username Input */}
                    <div className="mb-4">
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Tên đăng nhập
                        </label>
                        <div className="relative">
                            <User className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                            <input
                                type="text"
                                placeholder="Nhập tên đăng nhập"
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                                disabled={loading}
                            />
                        </div>
                    </div>

                    {/* Password Input */}
                    <div className="mb-6">
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Mật khẩu
                        </label>
                        <div className="relative">
                            <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                            <input
                                type="password"
                                placeholder="Nhập mật khẩu"
                                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                                disabled={loading}
                            />
                        </div>
                    </div>

                    {/* Login Button */}
                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 transition flex items-center justify-center gap-2 font-medium shadow-md hover:shadow-lg disabled:bg-gray-400 disabled:cursor-not-allowed"
                    >
                        {loading ? (
                            <>
                                <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                                <span>Đang đăng nhập...</span>
                            </>
                        ) : (
                            <>
                                <LogIn className="w-5 h-5" />
                                <span>Đăng nhập</span>
                            </>
                        )}
                    </button>

                    {/* Register Link */}
                    <p className="text-center mt-6 text-sm text-gray-600">
                        Chưa có tài khoản?{" "}
                        <Link
                            to="/register"
                            className="text-blue-600 hover:text-blue-700 font-medium hover:underline"
                        >
                            Đăng ký ngay
                        </Link>
                    </p>
                </form>

                {/* Footer */}
                <p className="text-center mt-4 text-xs text-gray-500">
                    © 2025 Hệ thống quản lý điểm
                </p>
            </div>
        </div>
    );
}