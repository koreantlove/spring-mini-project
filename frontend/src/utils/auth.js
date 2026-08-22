export const getAccessToken = () => {
  return localStorage.getItem('accessToken')
}

export const getJwtPayload = () => {
  const token = getAccessToken()

  if (!token) {
    return null
  }

  try {
    const payload = token.split('.')[1]

    return JSON.parse(
      atob(payload.replace(/-/g, '+').replace(/_/g, '/'))
    )
  } catch (error) {
    console.error('JWT parse 실패:', error)
    return null
  }
}

export const getCurrentUsername = () => {
  const payload = getJwtPayload()
  return payload?.sub ?? null
}

export const hasRole = (role) => {
  const payload = getJwtPayload()
  const roles = payload?.role ?? []
  return roles.includes(role)
}

export const isAdmin = () => {
  return hasRole('ROLE_ADMIN')
}
