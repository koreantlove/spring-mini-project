import api from './axios'

export const getComments = (boardId) => {
  return api.get(`/api/boards/${boardId}/comments`)
}

export const createComment = (boardId, data) => {
  return api.post(`/api/boards/${boardId}/comments`, data)
}

export const updateComment = (boardId, commentId, data) => {
  return api.put( `/api/boards/${boardId}/comments/${commentId}`,data)
}

export const deleteComment = (boardId, commentId) => {
  return api.delete(`/api/boards/${boardId}/comments/${commentId}`)
}
