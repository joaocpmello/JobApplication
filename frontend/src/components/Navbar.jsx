import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "./Navbar.css";

export default function Navbar() {
    const { user, isAuthenticated, isCompany, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    return (
        <nav className="navbar">
            <Link to="/" className="navbar-brand">
                💼 JobBoard
            </Link>

            <div className="navbar-links">
                <Link to="/jobs">Vagas</Link>

                {isAuthenticated && isCompany && (
                    <>
                        <Link to="/company/jobs">Minhas Vagas</Link>
                        <Link to="/company/profile">Empresa</Link>
                    </>
                )}

                {isAuthenticated && !isCompany && (
                    <Link to="/my-applications">Minhas Candidaturas</Link>
                )}
            </div>

            <div className="navbar-actions">
                {isAuthenticated ? (
                    <div className="navbar-user">
                        <span className="navbar-username">{user.name}</span>
                        <button className="btn-logout" onClick={handleLogout}>Sair</button>
                    </div>
                ) : (
                    <div className="navbar-auth">
                        <Link to="/login" className="btn-nav btn-outline">Login</Link>
                        <Link to="/register" className="btn-nav btn-primary">Cadastro</Link>
                    </div>
                )}
            </div>
        </nav>
    );
}