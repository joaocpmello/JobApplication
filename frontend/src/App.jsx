import { Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute, PublicOnlyRoute } from "./components/ProtectedRoute";
import Navbar from "./components/Navbar";

import Login from "./pages/Login";
import Register from "./pages/Register";
import Jobs from "./pages/Jobs";
import JobDetail from "./pages/JobDetail";
import MyApplications from "./pages/MyApplications";
import CompanyJobs from "./pages/CompanyJobs";
import CompanyApplications from "./pages/CompanyApplications";
import CompanyProfile from "./pages/CompanyProfile";

import "./App.css";

export default function App() {
    return (
        <AuthProvider>
            <Navbar />
            <main>
                <Routes>
                    <Route path="/" element={<Jobs />} />
                    <Route path="/jobs" element={<Jobs />} />
                    <Route path="/jobs/:id" element={<JobDetail />} />

                    <Route path="/login" element={<PublicOnlyRoute><Login /></PublicOnlyRoute>} />
                    <Route path="/register" element={<PublicOnlyRoute><Register /></PublicOnlyRoute>} />

                    <Route path="/my-applications" element={<ProtectedRoute><MyApplications /></ProtectedRoute>} />
                    <Route path="/company/jobs" element={<ProtectedRoute><CompanyJobs /></ProtectedRoute>} />
                    <Route path="/company/jobs/:jobId/applications" element={<ProtectedRoute><CompanyApplications /></ProtectedRoute>} />
                    <Route path="/company/profile" element={<ProtectedRoute><CompanyProfile /></ProtectedRoute>} />
                </Routes>
            </main>
        </AuthProvider>
    );
}