import axios from '../api/axios';

const LOGIN_URL    = '/auth/login';
const REGISTER_URL = '/auth/register';
const REFRESH_URL  = '/auth/refresh-token';

export const authService = {
  login: async (email, password) => {
    const { data } = await axios.post(LOGIN_URL, { email, password });

    // ――― сохраняем всё, что понадобится фронту ―――
    localStorage.setItem('accessToken',  data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    localStorage.setItem('userId',       data.id);
    localStorage.setItem('username',     data.username);     // ← НОВОЕ

    return data;
  },

  register: async (username, email, password) => {
    const { data } = await axios.post(REGISTER_URL, { username, email, password });
    return data;
  },

  logout: () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');                      // ← НОВОЕ
  },

  refresh: async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    const { data } = await axios.post(REFRESH_URL, { refreshToken });
    localStorage.setItem('accessToken', data.accessToken);
    return data.accessToken;
  }
};
