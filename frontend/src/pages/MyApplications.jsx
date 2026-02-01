import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { applicationService } from "../services/api";
import "./MyApplications.css";

export default function MyApplications() {
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetch = async () => {
            try {
                const { data } = await applicationService.getMyApplications();
                setApplications(data);
            } catch {
                setError("Erro ao buscar candidaturas");
            } finally {
                setLoading(false);
            }
        };
        fetch();
    }, []);

    const statusConfig = {
        PENDING:   { label: "Aguardando",  color: "#f39c12" },
        REVIEWING: { label: "Em análise",  color: "#3498db" },
        APPROVED:  { label: "Aprovado",    color: "#2ecc71" },
        REJECTED:  { label: "Rejeitado",   color: "#e74c3c" },
    };

    const handleDelete = async (id) => {
        if (!confirm("Deseja retirar esta candidatura?")) return;
        try {
            await applicationService.delete(id);
            setApplications((prev) => prev.filter((a) => a.id !== id));
        } catch {
            setError("Erro ao remover candidatura");
        }
    };

    if (loading) return <div className="app-loading">Carregando...</div>;

    return (
        <div className="my-apps-container">
            <h1 className="my-apps-title">Minhas Candidaturas</h1>

            {error && <div className="my-apps-error">{error}</div>}

            {applications.length === 0 && (
                <div className="my-apps-empty">
                    <p>Você ainda não se candidatou a nenhuma vaga.</p>
                    <Link to="/jobs" className="btn-go-jobs">Ver Vagas</Link>
                </div>
            )}

            <div className="my-apps-list">
                {applications.map((app) => {
                    const st = statusConfig[app.status] || { label: app.status, color: "#aaa" };
                    return (
                        <div key={app.id} className="my-app-card">
                            <div className="my-app-header">
                                <div>
                                    <Link to={`/jobs/${app.job?.id}`} className="my-app-title">
                                        {app.job?.title}
                                    </Link>
                                    <p className="my-app-company">{app.job?.company?.name}</p>
                                </div>
                                <span className="my-app-status" style={{ color: st.color, borderColor: st.color }}>
                  {st.label}
                </span>
                            </div>

                            {app.coverLetter && (
                                <p className="my-app-cover">"{app.coverLetter}"</p>
                            )}

                            <div className="my-app-footer">
                <span className="my-app-date">
                  Candidatura em {new Date(app.createdAt).toLocaleDateString("pt-BR")}
                </span>
                                <button className="btn-remove" onClick={() => handleDelete(app.id)}>
                                    Remover
                                </button>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}