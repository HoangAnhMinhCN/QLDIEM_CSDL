import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

export default function Register() {
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [birthday, setBirthday] = useState("");
    const [gender, setGender] = useState("Nam");
    const [role, setRole] = useState("student");

    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            const payload = {
                userName: username,
                userPassword: password,
                birthday,
                gender,
            };

            if (role === "student") payload.studentName = name;
            else payload.teacherName = name;

            await api.post(`/api/auth/register/${role}`, payload);

            alert("Đăng ký thành công! Hãy đăng nhập lại.");
            navigate("/login");
        } catch (err) {
            alert(
                err.response?.data || "Đăng ký thất bại! Vui lòng kiểm tra thông tin."
            );
            console.error(err);
        }
    };

    return (
        <div className="min-h-screen w-screen flex items-center justify-center bg-gray-100 py-12 px-4 sm:px-6 lg:px-8">
            <form
                onSubmit={handleRegister}
                className="bg-white p-8 rounded-2xl shadow-lg w-full max-w-md mx-auto"
            >
                <h2 className="text-2xl font-bold mb-6 text-center">Đăng ký</h2>

                <input
                    type="text"
                    placeholder="Họ và tên"
                    className="w-full border p-2 rounded mb-3"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <input
                    type="text"
                    placeholder="Tên đăng nhập"
                    className="w-full border p-2 rounded mb-3"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Mật khẩu"
                    className="w-full border p-2 rounded mb-3"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <input
                    type="date"
                    className="w-full border p-2 rounded mb-3"
                    value={birthday}
                    onChange={(e) => setBirthday(e.target.value)}
                />

                <select
                    className="w-full border p-2 rounded mb-3"
                    value={gender}
                    onChange={(e) => setGender(e.target.value)}
                >
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                </select>

                <select
                    className="w-full border p-2 rounded mb-5"
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                >
                    <option value="student">Học sinh / Sinh viên</option>
                    <option value="teacher">Giáo viên</option>
                </select>

                <button
                    type="submit"
                    className="w-full bg-green-500 text-white py-2 rounded"
                >
                    Đăng ký
                </button>

                <p className="text-center mt-3 text-sm">
                    Đã có tài khoản?{" "}
                    <Link to="/login" className="text-blue-500 hover:underline">
                        Đăng nhập
                    </Link>
                </p>
            </form>
        </div>
    );
}
