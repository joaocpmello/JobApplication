import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { jobService } from "../services/api";
import "./CompanyJobs.css";

export default function CompanyJobs() {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [showForm, setShowForm] = useState(false);
    const [form, setForm] = useState({ title: "", description: "", location: "", salaryMin: "", salaryMax: "" });
    const [creating, setCreating] = useState(false);

    const fetchJobs = async () => {
        try {
            const { data } = await jobService.getMyJobs({ page: 0, size: 50 });
            setJobs(data.content);
        } catch {
            setError("Erro ao buscar vagas");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => { fetchJobs(); }, []);

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleCreate = async (e) => {
        e.preventDefault();
        setCreating(true);
        setError("");
        try {
            const payload = {
                ...form,
                salaryMin: form.salaryMin ? Number(form.salaryMin) : null,
                salaryMax: form.salaryMax ? Number(form.salaryMax) : null,
            };
            const { data } = await jobService.create(payload);
            setJobs((prev) => [data, ...prev]);
            setForm({ title: "", description: "", location: "", salaryMin: "", salaryMax: "" });
            setShowForm(false);
        } catch (err) {
            setError(err.response?.data?.error || "Erro ao criar vaga");
        } finally {
            setCreating(false);
        }
    };

    const handleDelete = async (id) => {
        if (!confirm("Deseja deletar esta vaga?")) return;
        try {
            await jobService.delete(id);
            setJobs((prev) => prev.filter((j) => j.id !== id));
        } catch {
            setError("Erro ao deletar vaga");
        }
    };

    const statusConfig = {
        OPEN:   { label: "Aberta",    color: "#2ecc71" },
        CLOSED: { label: "Fechada",   color: "#e74c3c" },
        HIRED:  { label: "Contratado", color: "#f39c12" },
    };

    if (loading) return <div className="cj-loading">Carregando...</div>;

    return (
        <div className="cj-container">
            <div className="cj-header">
                <h1>Minhas Vagas</h1>
                <button className="btn-new-job" onClick={() => setShowForm(!showForm)}>
                    {showForm ? "Cancelar" : "+ Nova Vaga"}
                </button>
            </div>

            {error && <div className="cj-error">{error}</div>}

            {showForm && (
                <div className="cj-form-card">
                    <h3>Nova Vaga</h3>
                    <form onSubmit={handleCreate}>
                        <div className="form-row">
                            <div className="form-group">
                                <label>Título *</label>
                                <input type="text" name="title" value={form.title} onChange={handleChange} placeholder="Ex: Desenvolvedor Java" required />
                            </div>
                            <div className="form-group">
                                <label>Localização *</label>
                                <input type="text" name="location" value={form.location} onChange={handleChange} placeholder="Ex: São Paulo - SP" required />
                            </div>
                        </div>

                        <div className="form-row">
                            <div className="form-group">
                                <label>Salário Mínimo (R$)</label>
                                <input type="number" name="salaryMin" value={form.salaryMin} onChange={handleChange} placeholder="3000" />
                            </div>
                            <div className="form-group">
                                <label>Salário Máximo (R$)</label>
                                <input type="number" name="salaryMax" value={form.salaryMax} onChange={handleChange} placeholder="5000" />
                            </div>
                        </div>

                        <div className="form-group">
                            <label>Descrição</label>
                            <textarea name="description" value={form.description} onChange={handleChange} placeholder="Descreva a vaga..." rows={3} />
                        </div>

                        <button type="submit" className="btn-create" disabled={creating}>
                            {creating ? "Criando..." : "Criar Vaga"}
                        </button>
                    </form>
                </div>
            )}

            {jobs.length === 0 && (
                <div className="cj-empty">Você ainda não criou nenhuma vaga.</div>
            )}

            <div className="cj-list">
                {jobs.map((job) => {
                    const st = statusConfig[job.status] || { label: job.status, color: "#aaa" };
                    return (
                        <div key={job.id} className="cj-card">
                            <div className="cj-card-header">
                                <div>
                                    <h3>{job.title}</h3>
                                    <p className="cj-location">📍 {job.location}</p>
                                </div>
                                <span className="cj-status" style={{ color: st.color, borderColor: st.color }}>
                  {st.label}
                </span>
                            </div>

                            {job.salaryMin && job.salaryMax && (
                                <p className="cj-salary">💰 R$ {Number(job.salaryMin).toLocaleString()} – R$ {Number(job.salaryMax).toLocaleString()}</p>
                            )}

                            <div className="cj-card-footer">
                                <Link to={`/company/jobs/${job.id}/applications`} className="btn-view-apps">
                                    Ver Candidaturas
                                </Link>
                                <button className="btn-del" onClick={() => handleDelete(job.id)}>Deletar</button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}