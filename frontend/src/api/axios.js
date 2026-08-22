import axios from 'axios'
import { logout } from '../utils/auth'
import router from '../router'

let refreshPromise = null

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')

    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

api.interceptors.response.use(
    (response) => response,

    async (error) => {

        const originalRequest = error.config

        if ( error.response?.status === 401 &&
            !originalRequest._retry &&
            !originalRequest._isRefreshRequest ) {

            originalRequest._retry = true

            const refreshToken = localStorage.getItem('refreshToken')

            if (!refreshToken) {
                return Promise.reject(error)
            }

            try {

                if (!refreshPromise) {
                    refreshPromise = api.post(
                        '/api/users/refresh',
                        {
                            refreshToken
                        },
                        {
                            _isRefreshRequest: true
                        }
                    )
                    .finally(() => {
                        refreshPromise = null
                    })
                }
                const response = await refreshPromise

                const newAccessToken = response.data.data.accessToken

                localStorage.setItem('accessToken', newAccessToken )

                console.log('Access Token 재발급 성공')

                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`
                return api(originalRequest)

            } catch (refreshError) {

                console.error( 'Refresh Token 처리 실패', refreshError)
                logout()

                if (router.currentRoute.value.path !== '/login') {
                    router.push('/login')
                }

                return Promise.reject(refreshError)
            }
        }

        return Promise.reject(error)
    }
)

export default api
