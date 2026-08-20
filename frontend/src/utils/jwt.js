import { jwtDecode } from 'jwt-decode'

export const getToken = () => {
  return localStorage.getItem('accessToken')
}

export const getTokenPayload = () => {
  const token = getToken()

  if (!token) {
    return null
  }

  try {
    return jwtDecode(token)
  } catch (error) {
    console.error('JWT decode failed:', error)
    return null
  }
}

export const getRole = () => {
  const payload = getTokenPayload()

  if (!payload) {
    return null
  }

  return payload.role
}

export const isAdmin = () => {
  return getRole() === 'ROLE_ADMIN'
}

export const isUser = () => {
  return getRole() === 'ROLE_USER'
}
