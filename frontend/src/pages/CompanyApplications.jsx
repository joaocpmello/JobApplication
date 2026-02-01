import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { applicationService } from "../services/api";
import "./CompanyApplications.css";

const STATUS_OPTIONS = ["PENDING", "REVIEWING", "APPROVED", "REJECTED"];

const statusConfig = {
    PENDING:   { label: "Aguardando",  color: "#f39c12" },
    REVIEWING: { label: "Em análise",  color: "#3498db" },
    APPROVED:  { label: "Aprovado",    color: "#2ecc71" },
    REJECTED:  { label: "Rejeitado",   color: "#e74c3c" },
};

export default function CompanyApplications() {
    const { jobId } = useParams();
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [filterStatus, setFilterStatus] = useState("");

    useEffect(() => {
        const fetch = async () => {
            try {
                const params = { page: 0, size: 50 };
                if (filterStatus) params.status = filterStatus;
                const { data } = await applicationService.getByJob(jobId, params);
                setApplications(data.content);
            } catch {
                setError("Erro ao buscar candidaturas");
            } finally {
                setLoading(false);
            }
        };
        fetch();
    }, [jobId, filterStatus]);

    const handleStatusChange = async (id, newStatus) => {
        try {
            await applicationService.updateStatus(id, { status: newStatus });
            setApplications((prev) =>
                prev.map((a) => (a.id === id ? { ...a, status: newStatus } : a))
            );
        } catch {
            setError("Erro ao atualizar status");
        }
    };

    if (loading) return <div className="ca-loading">Carregando...</div>;

    return (
        <div className="ca-container">
            <button className="ca-back" onClick={() => navigate("/company/jobs")}>← Voltar</button>

            <div className="ca-header">
                <h1>Candidaturas da Vaga</h1>
                <select
                    className="ca-filter"
                    value={filterStatus}
                    onChange={(e) => setFilterStatus(e.target.value)}
                >
                    <option value="">Todos</option>
                    {STATUS_OPTIONS.map((s) => (
                        <option key={s} value={s}>{statusConfig[s].label}</option>
                    ))}
                </select>
            </div>

            {error && <div className="ca-error">{error}</div>}

            {applications.length === 0 && (
                <div className="ca-empty">Nenhuma candidatura encontrada.</div>
            )}

            <div className="ca-list">
                {applications.map((app) => {
                    const st = statusConfig[app.status] || { label: app.status, color: "#aaa" };
                    return (
                        <div key={app.id} className="ca-card">
                            <div className="ca-card-header">
                                <div>
                                    <h3>{app.candidate?.name}</h3>
                                    <p className="ca-email">{app.candidate?.email}</p>
                                </div>
                                <span className="ca-status" style={{ color: st.color, borderColor: st.color }}>
                  {st.label}
                </span>
                            </div>

                            {app.coverLetter && (
                                <p className="ca-cover">"{app.coverLetter}"</p>
                            )}

                            <div className="ca-card-footer">
                <span className="ca-date">
                  {new Date(app.createdAt).toLocaleDateString("pt-BR")}
                </span>

                                <div className="ca-actions">
                                    <select
                                        className="ca-status-select"
                                        value={app.status}
                                        onChange={(e) => handleStatusChange(app.id, e.target.value)}
                                    >
                                        {STATUS_OPTIONS.map((s) => (
                                            <option key={s} value={s}>{statusConfig[s].label}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}