// src/api/axios.js
import axios from 'axios';
import { authService } from '../services/authService';

const axiosInstance = axios.create({
  baseURL: '/api',               // proxy на localhost:8080
  headers: { 'Content-Type': 'application/json' }
});

/* ---------- запрос: подставляем токен ---------- */
axiosInstance.interceptors.request.use(config => {
  const token = localStorage.getItem('accessToken');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

/* ——— helpers для очереди запросов, ожидающих refresh ——— */
let isRefreshing = false;
let subscribers  = [];

const subscribe = cb => subscribers.push(cb);
const onRefreshed = token => {
  subscribers.forEach(cb => cb(token));
  subscribers = [];
};

/* ---------- ответ: если 401 → пробуем refresh ---------- */
axiosInstance.interceptors.response.use(
  res => res,
  async error => {
    const { config, response } = error;
    if (!response || response.status !== 401 || config.__isRetryRequest) {
      // не 401 или уже повторяли — отдаём ошибку дальше
      return Promise.reject(error);
    }

    /* маркируем запрос, чтобы не зациклиться */
    config.__isRetryRequest = true;

    /* ---- запускаем refresh один раз ---- */
    if (!isRefreshing) {
      isRefreshing = true;
      try {
        const newToken = await authService.refresh();   // POST /auth/refresh-token
        isRefreshing = false;
        onRefreshed(newToken);                          // разбудим «ожидающих»
      } catch (refreshErr) {
        isRefreshing = false;
        authService.logout();                           // refresh недействителен
        window.location.href = '/login';
        return Promise.reject(refreshErr);
      }
    }

    /* ---- ставим запрос в очередь до завершения refresh ---- */
    return new Promise(resolve => {
      subscribe(token => {
        config.headers.Authorization = `Bearer ${token}`;
        resolve(axiosInstance(config)); // повторяем запрос
      });
    });
  }
);

export default axiosInstance;
