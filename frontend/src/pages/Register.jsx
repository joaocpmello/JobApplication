import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { authService } from "../services/api";
import "./AuthPages.css";

export default function Register() {
    const [form, setForm] = useState({ name: "", email: "", password: "", role: "CANDIDATE" });
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");
        setLoading(true);

        try {
            await authService.register(form);
            navigate("/login");
        } catch (err) {
            const data = err.response?.data;
            if (data?.password) setError(data.password);
            else if (data?.email) setError(data.email);
            else if (data?.error) setError(data.error);
            else setError("Erro ao criar conta");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="auth-container">
            <div className="auth-card">
                <h1 className="auth-title">💼 JobBoard</h1>
                <p className="auth-subtitle">Crie sua conta</p>

                {error && <div className="auth-error">{error}</div>}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Nome</label>
                        <input
                            type="text"
                            name="name"
                            placeholder="João Silva"
                            value={form.name}
                            onChange={handleChange}
                            required
                        />
                    </div>

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
                            placeholder="Mínimo 8 caracteres"
                            value={form.password}
                            onChange={handleChange}
                            required
                        />
                        <span className="form-hint">
              Use letras maiúsculas, minúsculas, números e caracteres especiais (@#$%^&+=!)
            </span>
                    </div>

                    <div className="form-group">
                        <label>Tipo de Conta</label>
                        <div className="radio-group">
                            <label className={`radio-option ${form.role === "CANDIDATE" ? "active" : ""}`}>
                                <input
                                    type="radio"
                                    name="role"
                                    value="CANDIDATE"
                                    checked={form.role === "CANDIDATE"}
                                    onChange={handleChange}
                                />
                                🧑‍💼 Candidato
                            </label>

                            <label className={`radio-option ${form.role === "COMPANY" ? "active" : ""}`}>
                                <input
                                    type="radio"
                                    name="role"
                                    value="COMPANY"
                                    checked={form.role === "COMPANY"}
                                    onChange={handleChange}
                                />
                                🏢 Empresa
                            </label>
                        </div>
                    </div>

                    <button type="submit" className="btn-submit" disabled={loading}>
                        {loading ? "Cadastrando..." : "Cadastrar"}
                    </button>
                </form>

                <p className="auth-link">
                    Já tem conta? <Link to="/login">Faça login</Link>
                </p>
            </div>
        </div>
    );
}