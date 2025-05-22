import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import Navbar        from '../components/Navbar';
import Login         from '../pages/Login';
import Register      from '../pages/Register';
import TasksList     from '../pages/TasksList';
import ChangePassword from '../pages/ChangePassword';   // ← НОВОЕ

const AppRoutes = () => {
  const isAuth = !!localStorage.getItem('accessToken');

  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route
          path="/"
          element={<Navigate to={isAuth ? '/tasks' : '/login'} />}
        />

        <Route path="/login"    element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route
          path="/tasks"
          element={isAuth ? <TasksList /> : <Navigate to="/login" />}
        />

        {/* ── защищённый маршрут смены пароля ── */}
        <Route
          path="/change-password"
          element={isAuth ? <ChangePassword /> : <Navigate to="/login" />}
        />

        {/* fallback */}
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRoutes;
