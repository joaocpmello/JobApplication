import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { authService } from "../services/api";
import "./AuthPages.css";

export default function Login() {
    const [form, setForm] = useState({ email: "", password: "" });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const { login } = useAuth();
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            const { data } = await authService.login(form);
            // Salvar role junto com os dados do usuário
            const userData = { ...data.user, role: decodeRole(data.token) };
            login(userData, data.token);
            navigate("/jobs");
        } catch (err) {
            setError(err.response?.data?.error || "Erro ao realizar login");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h1 className="auth-title">💼 JobBoard</h1>
                <p className="auth-subtitle">Faça login na sua conta</p>

                {error && <div className="auth-error">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            name="email"
                            placeholder="seu@email.com"
                            value={form.email}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Senha</label>
                        <input
                            type="password"
                            name="password"
                            placeholder="sua senha"
                            value={form.password}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <button type="submit" className="btn-submit" disabled={loading}>
                        {loading ? "Entrando..." : "Entrar"}
                    </button>
                </form>

                <p className="auth-link">
                    Não tem conta? <Link to="/register">Cadastre-se</Link>
                </p>
            </div>
        </div>
    );
}

function decodeRole(token) {
    try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        return payload.role;
    } catch {
        return null;
    }
}