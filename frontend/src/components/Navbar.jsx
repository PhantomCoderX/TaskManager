import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';

const Navbar = () => {
  const navigate = useNavigate();
  const isAuth = !!localStorage.getItem('accessToken');

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light mb-4">
      <div className="container">
        <Link className="navbar-brand" to="/">TaskManager</Link>

        <div className="collapse navbar-collapse">
          <ul className="navbar-nav ms-auto">
            {isAuth ? (
              <>
                {/* кнопка смены пароля */}
                <li className="nav-item">
                  <Link className="nav-link" to="/change-password">
                    Change&nbsp;Password
                  </Link>
                </li>

                <li className="nav-item">
                  <button className="btn btn-link nav-link" onClick={handleLogout}>
                    Logout
                  </button>
                </li>
              </>
            ) : (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">
                    Login
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">
                    Register
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
