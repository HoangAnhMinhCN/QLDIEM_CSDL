import React from "react";
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Navigate
} from "react-router-dom";

import Login from "./pages/Login.jsx";
import Register from "./pages/Register.jsx";
import Teacher from "./pages/Teacher.jsx";
import Student from "./pages/Student.jsx";
import "./style.css";

// Component bảo vệ route - Kiểm tra token trong localStorage
function ProtectedRoute({ children, allowedRole }) {
    const token = localStorage.getItem("token");
    const userStr = localStorage.getItem("user");

    if (!token) {
        // Chưa đăng nhập -> về trang login
        return <Navigate to="/login" replace />;
    }

    if (allowedRole) {
        try {
            const user = JSON.parse(userStr);
            const roles = user.roles || user.authorities || [];

            let hasRole = false;
            roles.forEach((role) => {
                const roleName = typeof role === 'string' ? role : (role.authority || role.role);
                if (roleName === `ROLE_${allowedRole.toUpperCase()}` || roleName === allowedRole.toUpperCase()) {
                    hasRole = true;
                }
            });

            if (!hasRole) {
                // Không có quyền -> về trang login
                return <Navigate to="/login" replace />;
            }
        } catch (err) {
            console.error("Error parsing user:", err);
            return <Navigate to="/login" replace />;
        }
    }

    return children;
}

export default function App() {
    return (
        <Router>
            <Routes>
                {/* Trang chủ -> chuyển về register */}
                <Route path="/" element={<Navigate to="/register" replace />} />

                {/* Trang đăng ký */}
                <Route path="/register" element={<Register />} />

                {/* Trang đăng nhập */}
                <Route path="/login" element={<Login />} />

                {/* Trang giáo viên - Được bảo vệ */}
                <Route
                    path="/teacher"
                    element={
                        <ProtectedRoute allowedRole="teacher">
                            <Teacher />
                        </ProtectedRoute>
                    }
                />

                {/* Trang sinh viên - Được bảo vệ */}
                <Route
                    path="/student"
                    element={
                        <ProtectedRoute allowedRole="student">
                            <Student />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </Router>
    );
}