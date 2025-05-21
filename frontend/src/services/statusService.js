import axios from '../api/axios';

/**
 * CRUD нам пока не нужен, берём только список.
 * Эндпоинт совпадает с бэкендом: GET /api/statuses
 */
export const statusService = {
  getAll: async () => {
    const { data } = await axios.get('/statuses'); // ← если у вас префикс /api, поменяйте на '/api/statuses'
    return data;                                   // ожидаем [{ id, name, ... }]
  }
};
