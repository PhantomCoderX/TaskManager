import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';

const ChangePassword = () => {
  const [oldPass, setOldPass] = useState('');
  const [newPass, setNewPass] = useState('');
  const [msg,     setMsg]     = useState('');
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await authService.changePassword(oldPass, newPass);
      setMsg('Password updated — please login again');
      authService.logout();
      setTimeout(() => navigate('/login'), 1500);
    } catch (err) {
      setMsg(err.response?.data || 'Error updating password');
    }
  };

  return (
    <div className="container" style={{ maxWidth: 400 }}>
      <h3 className="mt-4 mb-3">Change Password</h3>

      {msg && <div className="alert alert-info">{msg}</div>}

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Current password</label>
          <input
            type="password"
            className="form-control"
            value={oldPass}
            onChange={e => setOldPass(e.target.value)}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">New password</label>
          <input
            type="password"
            className="form-control"
            value={newPass}
            onChange={e => setNewPass(e.target.value)}
            required
          />
        </div>

        <button className="btn btn-primary w-100">Save</button>
      </form>
    </div>
  );
};

export default ChangePassword;
