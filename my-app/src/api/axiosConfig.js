import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
});

// Chỉ thêm token cho NON-auth endpoints
api.interceptors.request.use(
    (config) => {
        // KHÔNG thêm token nếu URL chứa /api/auth
        if (config.url && !config.url.includes("/api/auth")) {
            const token = localStorage.getItem("token");
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;