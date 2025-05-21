import axios from '../api/axios';

const TASKS_URL = '/tasks';

export const taskService = {
  /* получить все задачи */
  getAll:   async ()               => (await axios.get(TASKS_URL)).data,

  /* создать новую задачу */
  create:   async task             => (await axios.post(TASKS_URL, task)).data,

  /* 🔄 обновить задачу (в т. ч. только статус) */
  update:   async (id, partial)    => (await axios.put(`${TASKS_URL}/${id}`, partial)).data,

  /* удалить задачу */
  delete:   async id               => axios.delete(`${TASKS_URL}/${id}`)
};
