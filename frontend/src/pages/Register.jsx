// src/pages/Register.jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authService }        from '../services/authService';

const Register = () => {
  const [username, setUsername] = useState('');
  const [email,    setEmail]    = useState('');
  const [password, setPassword] = useState('');
  const [error,    setError]    = useState(null);
  const nav = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await authService.register(username, email, password);
      nav('/login');
    } catch (err) {
      setError(err.response?.data || 'Registration failed');
    }
  };

  return (
    <div className="container" style={{ maxWidth: 400, marginTop: '2rem' }}>
      <h2>Register</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={handleSubmit}>
        <input
          className="form-control mb-2"
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />
        <input
          type="email"
          className="form-control mb-2"
          placeholder="Email"
          value={email}
          onChange={e => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          className="form-control mb-2"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
        <button className="btn btn-primary w-100">Register</button>
      </form>
      <p className="mt-3 text-center">
        Have account? <Link to="/login">Login</Link>
      </p>
    </div>
  );
};

export default Register;
