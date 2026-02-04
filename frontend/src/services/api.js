import axios from "axios";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

const api = axios.create({
    baseURL: API_URL,
    headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
});

api.interceptors.response.use(
    (res) => res,
    (err) => {
        if (err.response?.status === 401) {
            localStorage.removeItem("token");
            localStorage.removeItem("user");
            window.location.href = "/login";
        }
        return Promise.reject(err);
    }
);

// ─── Auth ───────────────────────────────────
export const authService = {
    register: (data) => api.post("/api/auth/register", data),
    login: (data) => api.post("/api/auth/login", data),
};

// ─── Users ──────────────────────────────────
export const userService = {
    getMe: () => api.get("/api/users/me"),
};

// ─── Companies ──────────────────────────────
export const companyService = {
    create: (data) => api.post("/api/companies", data),
    getById: (id) => api.get(`/api/companies/${id}`),
    getMyCompany: () => api.get("/api/companies/my-company"),
    update: (data) => api.put("/api/companies", data),
};

// ─── Jobs ───────────────────────────────────
export const jobService = {
    create: (data) => api.post("/api/jobs", data),
    getById: (id) => api.get(`/api/jobs/${id}`),
    search: (params) => {
        const cleanParams = Object.fromEntries(
            Object.entries(params).filter(([_, v]) => v !== "" && v !== null && v !== undefined)
        );

        return api.get("/api/jobs", { params: cleanParams });
    },

    getMyJobs: (params) => api.get("/api/jobs/my-jobs", { params }),
    update: (id, data) => api.put(`/api/jobs/${id}`, data),
    delete: (id) => api.delete(`/api/jobs/${id}`),
};

// ─── Applications ───────────────────────────
export const applicationService = {
    create: (data) => api.post("/api/applications", data),
    getById: (id) => api.get(`/api/applications/${id}`),
    getMyApplications: () => api.get("/api/applications/my-applications"),
    getByJob: (jobId, params) => api.get(`/api/applications/job/${jobId}`, { params }),
    updateStatus: (id, data) => api.patch(`/api/applications/${id}/status`, data),
    delete: (id) => api.delete(`/api/applications/${id}`),
};

export default api;