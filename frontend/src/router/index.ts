import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '../views/LoginView.vue'
import BoardListView from '../views/BoardListView.vue'
import BoardDetailView from '../views/BoardDetailView.vue'

const router = createRouter({
  history: createWebHistory(),

  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/boards',
      name: 'boards',
      component: BoardListView,
      meta: {
              requiresAuth: true,
            },
    },
    {
      path: '/boards/:boardId',
      name: 'board-detail',
      component: BoardDetailView
    },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('accessToken')

  if (to.meta.requiresAuth && !token) {
    return {
      name: 'login',
    }
  }

  return true
})

export default router
