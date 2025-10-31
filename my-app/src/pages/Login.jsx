import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            // XÓA token cũ trước khi đăng nhập
            localStorage.removeItem("token");
            localStorage.removeItem("user");

            const response = await api.post("/api/auth/login", {
                username,
                password,
            });

            // Lưu thông tin đăng nhập
            localStorage.setItem("token", response.data.accessToken);
            localStorage.setItem("user", JSON.stringify(response.data));

            // Điều hướng dựa trên role
            const roles = response.data.roles || response.data.authorities;
            const isTeacher = roles.some(
                (role) => role.authority === "ROLE_TEACHER" || role === "ROLE_TEACHER"
            );

            if (isTeacher) {
                navigate("/teacher");
            } else {
                navigate("/student");
            }

            alert("Đăng nhập thành công!");
        } catch (err) {
            alert(
                err.response?.data || "Đăng nhập thất bại! Vui lòng kiểm tra thông tin."
            );
            console.error(err);
        }
    };

    return (
        <div className="flex items-center justify-center h-screen bg-gray-100">
            <form
                onSubmit={handleLogin}
                className="bg-white p-8 rounded-2xl shadow w-96"
            >
                <h2 className="text-2xl font-bold mb-6 text-center">Đăng nhập</h2>

                <input
                    type="text"
                    placeholder="Tên đăng nhập"
                    className="w-full border p-2 rounded mb-3"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />

                <input
                    type="password"
                    placeholder="Mật khẩu"
                    className="w-full border p-2 rounded mb-5"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                <button
                    type="submit"
                    className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
                >
                    Đăng nhập
                </button>

                <p className="text-center mt-3 text-sm">
                    Chưa có tài khoản?{" "}
                    <Link to="/register" className="text-blue-500 hover:underline">
                        Đăng ký
                    </Link>
                </p>
            </form>
        </div>
    );
}