import api from './axios'

export const getBoards = (params) => {
  return api.get('/api/boards', {
    params,
  })
}

export const getBoard = (boardId) => {
  return api.get(`/api/boards/${boardId}`)
}

export const updateBoard = (boardId, data) => {
  return api.put(`/api/boards/${boardId}`, data)
}

export const deleteBoard = (boardId) => {
  return api.delete(`/api/boards/${boardId}`)
}
