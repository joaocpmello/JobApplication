import { useState, useEffect } from "react";
import { companyService } from "../services/api";
import "./CompanyProfile.css";

export default function CompanyProfile() {
    const [company, setCompany] = useState(null);
    const [form, setForm] = useState({ name: "", description: "", cnpj: "", website: "" });
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [message, setMessage] = useState({ text: "", type: "" });
    const [isCreating, setIsCreating] = useState(false);

    useEffect(() => {
        const fetch = async () => {
            try {
                const { data } = await companyService.getMyCompany();
                setCompany(data);
                setForm({ name: data.name || "", description: data.description || "", cnpj: "", website: data.website || "" });
            } catch {
                setIsCreating(true);
            } finally {
                setLoading(false);
            }
        };
        fetch();
    }, []);

    const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setMessage({ text: "", type: "" });
        try {
            if (isCreating) {
                const { data } = await companyService.create(form);
                setCompany(data);
                setIsCreating(false);
                setMessage({ text: "Empresa criada com sucesso! 🎉", type: "success" });
            } else {
                const { data } = await companyService.update(form);
                setCompany(data);
                setMessage({ text: "Perfil atualizado com sucesso! ✅", type: "success" });
            }
        } catch (err) {
            setMessage({ text: err.response?.data?.error || "Erro ao salvar", type: "error" });
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <div className="cp-loading">Carregando...</div>;

    return (
        <div className="cp-container">
            <h1 className="cp-title">{isCreating ? "Criar Perfil da Empresa" : "Perfil da Empresa"}</h1>

            {message.text && (
                <div className={`cp-message ${message.type}`}>{message.text}</div>
            )}

            <div className="cp-card">
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Nome da Empresa *</label>
                        <input type="text" name="name" value={form.name} onChange={handleChange} placeholder="Tech Corp LTDA" required />
                    </div>

                    <div className="form-group">
                        <label>CNPJ</label>
                        <input type="text" name="cnpj" value={form.cnpj} onChange={handleChange} placeholder="00.000.000/0000-00" />
                    </div>

                    <div className="form-group">
                        <label>Website</label>
                        <input type="text" name="website" value={form.website} onChange={handleChange} placeholder="https://techcorp.com" />
                    </div>

                    <div className="form-group">
                        <label>Descrição</label>
                        <textarea name="description" value={form.description} onChange={handleChange} placeholder="Descreva sua empresa..." rows={4} />
                    </div>

                    <button type="submit" className="btn-save" disabled={saving}>
                        {saving ? "Salvando..." : isCreating ? "Criar Empresa" : "Salvar Alterações"}
                    </button>
                </form>
            </div>
        </div>
    );
}