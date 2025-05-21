import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login        from './pages/Login';
import Register     from './pages/Register';
import TasksList    from './pages/TasksList';

function PrivateRoute({ children }) {
  const token = localStorage.getItem('accessToken');
  return token ? children : <Navigate to="/login" />;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login"  element={<Login/>} />
        <Route path="/register" element={<Register/>} />
        <Route
          path="/tasks"
          element={
            <PrivateRoute>
              <TasksList/>
            </PrivateRoute>
          }
        />
        <Route path="*" element={<Navigate to="/tasks" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
