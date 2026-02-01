import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { jobService } from "../services/api";
import "./Jobs.css";

export default function Jobs() {
    const [jobs, setJobs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [filters, setFilters] = useState({ title: "", status: "", page: 0, size: 12 });

    const fetchJobs = async () => {
        setLoading(true);
        try {
            const { data } = await jobService.search(filters);
            setJobs(data.content);
        } catch (err) {
            setError("Erro ao buscar vagas");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchJobs();
    }, [filters]);

    const handleFilterChange = (e) => {
        setFilters((prev) => ({ ...prev, [e.target.name]: e.target.value, page: 0 }));
    };

    const statusLabel = {
        OPEN: { label: "Aberta", color: "#2ecc71" },
        CLOSED: { label: "Fechada", color: "#e74c3c" },
        HIRED: { label: "Contratado", color: "#f39c12" },
    };

    return (
        <div className="jobs-container">
            {/* Filtros */}
            <div className="jobs-filters">
                <input
                    type="text"
                    name="title"
                    placeholder="🔍 Buscar por título..."
                    value={filters.title}
                    onChange={handleFilterChange}
                    className="filter-input"
                />
                <select name="status" value={filters.status} onChange={handleFilterChange} className="filter-select">
                    <option value="">Todos os status</option>
                    <option value="OPEN">Abertas</option>
                    <option value="CLOSED">Fechadas</option>
                    <option value="HIRED">Contratado</option>
                </select>
            </div>

            {/* Listagem */}
            {loading && <div className="jobs-loading">Carregando vagas...</div>}
            {error && <div className="jobs-error">{error}</div>}

            {!loading && jobs.length === 0 && (
                <div className="jobs-empty">Nenhuma vaga encontrada.</div>
            )}

            <div className="jobs-grid">
                {jobs.map((job) => {
                    const st = statusLabel[job.status] || { label: job.status, color: "#aaa" };
                    return (
                        <Link to={`/jobs/${job.id}`} key={job.id} className="job-card">
                            <div className="job-card-header">
                                <h3>{job.title}</h3>
                                <span className="job-status" style={{ color: st.color, borderColor: st.color }}>
                  {st.label}
                </span>
                            </div>

                            <p className="job-company">{job.company?.name}</p>
                            <p className="job-location">📍 {job.location}</p>

                            {job.salaryMin && job.salaryMax && (
                                <p className="job-salary">
                                    💰 R$ {Number(job.salaryMin).toLocaleString()} – R$ {Number(job.salaryMax).toLocaleString()}
                                </p>
                            )}

                            <p className="job-date">
                                Publicada em {new Date(job.createdAt).toLocaleDateString("pt-BR")}
                            </p>
                        </Link>
                    );
                })}
            </div>
        </div>
    );
}