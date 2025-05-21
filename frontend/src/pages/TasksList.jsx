// src/pages/TasksList.jsx
import React, { useEffect, useMemo, useState } from 'react';
import { taskService } from '../services/taskService';
import { statusService } from '../services/statusService';

const TasksList = () => {
  /* ------------ state ------------ */
  const [tasks,    setTasks]    = useState([]);
  const [statuses, setStatuses] = useState([]);

  /* поля формы создания новой задачи */
  const [title,    setTitle]    = useState('');
  const [content,  setContent]  = useState('');
  const [statusId, setStatusId] = useState('');

  /* данные текущего пользователя */
  const username = localStorage.getItem('username');
  const loggedUserId = Number(localStorage.getItem('userId'));

  /* id → name мапа */
  const statusMap = useMemo(() => {
    const map = {};
    statuses.forEach(s => { map[s.id] = s.name; });
    return map;
  }, [statuses]);

  /* ------------ загрузка задач и статусов ------------ */
  const fetchData = async () => {
    const [allTasks, allStatuses] = await Promise.all([
      taskService.getAll(),
      statusService.getAll()
    ]);

    /* ==== оставляем задачи ТОЛЬКО текущего пользователя ==== */
    const ownedTasks = allTasks.filter(t => {
      if ('userId' in t)             return t.userId === loggedUserId;
      if (t.user && t.user.id)       return t.user.id === loggedUserId;
      return false; // отбрасываем, если нет явной принадлежности
    });

    setTasks(ownedTasks);
    setStatuses(allStatuses);

    /* дефолтный статус в форме */
    if (!statusId && allStatuses.length) {
      setStatusId(String(allStatuses[0].id));
    }
  };

  useEffect(() => { fetchData(); }, []);

  /* ------------ создание задачи ------------ */
  const handleAdd = async e => {
    e.preventDefault();
    await taskService.create({
      title,
      content,
      userId:   loggedUserId,
      statusId: Number(statusId)
    });
    setTitle('');
    setContent('');
    fetchData();
  };

  /* ------------ удаление задачи ------------ */
  const handleDelete = async id => {
    await taskService.delete(id);
    fetchData();
  };

  /* ------------ смена статуса у существующей задачи ------------ */
  const handleStatusChange = async (taskId, newStatusId) => {
    const original = tasks.find(t => t.id === taskId);
    if (!original) return;

    const { title, content, userId } = original;
    await taskService.update(taskId, {
      title,
      content,
      userId,
      statusId: Number(newStatusId)
    });

    fetchData();
  };

  /* читаемое имя статуса */
  const getStatusName = task =>
    task.status?.name        ??
    task.status              ??
    task.statusName          ??
    statusMap[task.statusId] ??
    `#${task.statusId ?? '?'}`;

  /* ------------ JSX ------------ */
  return (
    <div className="container">
      <h2>Tasks</h2>

      {username && (
        <p className="text-muted mb-3">
          Logged in as: <strong>{username}</strong>
        </p>
      )}

      {/* -------- форма добавления -------- */}
      <form onSubmit={handleAdd} className="mb-4">
        <input
          className="form-control mb-2"
          placeholder="Title"
          value={title}
          onChange={e => setTitle(e.target.value)}
          required
        />

        <textarea
          className="form-control mb-2"
          placeholder="Content"
          value={content}
          onChange={e => setContent(e.target.value)}
          required
        />

        <select
          className="form-select mb-3"
          value={statusId}
          onChange={e => setStatusId(e.target.value)}
          required
        >
          {statuses.map(s => (
            <option key={s.id} value={s.id}>{s.name}</option>
          ))}
        </select>

        <button className="btn btn-success">Add Task</button>
      </form>

      {/* -------- список задач -------- */}
      <ul className="list-group">
        {tasks.map(t => (
          <li
            key={t.id}
            className="list-group-item d-flex justify-content-between align-items-start"
          >
            <div>
              <h5 className="mb-1">
                {t.title}{' '}
                <select
                  className="form-select form-select-sm d-inline w-auto ms-2"
                  value={t.statusId ?? ''}
                  onChange={e => handleStatusChange(t.id, e.target.value)}
                >
                  {statuses.map(s => (
                    <option key={s.id} value={s.id}>{s.name}</option>
                  ))}
                </select>
              </h5>
              <p className="mb-1">{t.content}</p>
            </div>

            <button
              className="btn btn-danger btn-sm"
              onClick={() => handleDelete(t.id)}
            >
              Delete
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TasksList;
