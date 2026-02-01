import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { jobService, applicationService } from "../services/api";
import "./JobDetail.css";

export default function JobDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { isAuthenticated, isCandidate } = useAuth();

    const [job, setJob] = useState(null);
    const [loading, setLoading] = useState(true);
    const [coverLetter, setCoverLetter] = useState("");
    const [applying, setApplying] = useState(false);
    const [message, setMessage] = useState({ text: "", type: "" });

    useEffect(() => {
        const fetch = async () => {
            try {
                const { data } = await jobService.getById(id);
                setJob(data);
            } catch {
                setMessage({ text: "Vaga não encontrada", type: "error" });
            } finally {
                setLoading(false);
            }
        };
        fetch();
    }, [id]);

    const handleApply = async (e) => {
        e.preventDefault();
        setApplying(true);
        setMessage({ text: "", type: "" });
        try {
            await applicationService.create({ jobId: Number(id), coverLetter });
            setMessage({ text: "Candidatura enviada com sucesso! 🎉", type: "success" });
            setCoverLetter("");
        } catch (err) {
            setMessage({ text: err.response?.data?.error || "Erro ao candidatar-se", type: "error" });
        } finally {
            setApplying(false);
        }
    };

    if (loading) return <div className="detail-loading">Carregando...</div>;
    if (!job) return <div className="detail-loading">Vaga não encontrada.</div>;

    const statusLabel = { OPEN: "Aberta", CLOSED: "Fechada", HIRED: "Contratado" };
    const statusColor = { OPEN: "#2ecc71", CLOSED: "#e74c3c", HIRED: "#f39c12" };

    return (
        <div className="detail-container">
            <button className="detail-back" onClick={() => navigate("/jobs")}>← Voltar</button>

            <div className="detail-card">
                {/* Header */}
                <div className="detail-header">
                    <div>
                        <h1>{job.title}</h1>
                        <p className="detail-company">{job.company?.name}</p>
                    </div>
                    <span className="detail-status" style={{ color: statusColor[job.status], borderColor: statusColor[job.status] }}>
            {statusLabel[job.status]}
          </span>
                </div>

                <div className="detail-info">
                    <div className="detail-info-item">
                        <span className="detail-info-label">📍 Localização</span>
                        <span>{job.location}</span>
                    </div>
                    {job.salaryMin && job.salaryMax && (
                        <div className="detail-info-item">
                            <span className="detail-info-label">💰 Salário</span>
                            <span>R$ {Number(job.salaryMin).toLocaleString()} – R$ {Number(job.salaryMax).toLocaleString()}</span>
                        </div>
                    )}
                    <div className="detail-info-item">
                        <span className="detail-info-label">📅 Publicado em</span>
                        <span>{new Date(job.createdAt).toLocaleDateString("pt-BR")}</span>
                    </div>
                </div>

                {job.description && (
                    <div className="detail-description">
                        <h3>Descrição</h3>
                        <p>{job.description}</p>
                    </div>
                )}

                {isAuthenticated && isCandidate && job.status === "OPEN" && (
                    <div className="detail-apply">
                        <h3>Candidatar-se</h3>

                        {message.text && (
                            <div className={`detail-message ${message.type}`}>{message.text}</div>
                        )}

                        <form onSubmit={handleApply}>
              <textarea
                  placeholder="Escreva sua carta de apresentação (opcional)..."
                  value={coverLetter}
                  onChange={(e) => setCoverLetter(e.target.value)}
                  rows={4}
                  maxLength={1000}
              />
                            <span className="char-count">{coverLetter.length}/1000</span>
                            <button type="submit" className="btn-apply" disabled={applying}>
                                {applying ? "Enviando..." : "Candidatar-se"}
                            </button>
                        </form>
                    </div>
                )}

                {/* Se não autenticado, mostrar botão de login */}
                {!isAuthenticated && job.status === "OPEN" && (
                    <div className="detail-apply">
                        <p className="detail-login-hint">
                            <button className="btn-apply" onClick={() => navigate("/login")}>
                                Faça login para candidatar-se
                            </button>
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
}