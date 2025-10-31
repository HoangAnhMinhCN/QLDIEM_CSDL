import React, { useState } from 'react';
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
import './style.css';

export default function App() {
    // Lưu thông tin user sau khi đăng nhập
    const [user, setUser] = useState(null);

    // Hàm xử lý khi đăng nhập thành công
    const handleLoginSuccess = (loggedInUser) => {
        setUser(loggedInUser);
    };

    return (
        <Router>
            <Routes>
                {/* ✅ Trang mặc định: khi mở app, tự động chuyển sang trang đăng ký */}
                <Route path="/" element={<Navigate to="/register" replace />} />

                {/* Trang đăng ký */}
                <Route path="/register" element={<Register />} />

                {/* Trang đăng nhập:
                    - Nếu chưa đăng nhập → hiển thị form login
                    - Nếu đã đăng nhập → chuyển đến trang tương ứng */}
                <Route
                    path="/login"
                    element={
                        !user ? (
                            <Login onLoginSuccess={handleLoginSuccess} />
                        ) : (
                            <Navigate to={user.role === 'student' ? '/student' : '/teacher'} />
                        )
                    }
                />

                {/* Trang giáo viên */}
                <Route
                    path="/teacher"
                    element={
                        user && user.role === 'teacher' ? (
                            <Teacher />
                        ) : (
                            <Navigate to="/login" replace />
                        )
                    }
                />

                {/* Trang sinh viên */}
                <Route
                    path="/student"
                    element={
                        user && user.role === 'student' ? (
                            <Student />
                        ) : (
                            <Navigate to="/login" replace />
                        )
                    }
                />
            </Routes>
        </Router>
    );
}
