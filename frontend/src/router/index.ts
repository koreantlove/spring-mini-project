import { createRouter, createWebHistory } from 'vue-router'
import { isAuthenticated,isAdmin,} from '../utils/auth'

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
      component: BoardDetailView,
      meta: {
        requiresAuth: true,
      }
    },
  ],
})

router.beforeEach((to) => {
  const authenticated = isAuthenticated()

  if (to.meta.requiresAuth && !authenticated) {
    return {
        path: '/login',
        query: {
                redirect: to.fullPath,
              },
      }
  }

  if (to.meta.requiresAdmin && !isAdmin()) {
    return '/boards'
  }

  if (to.path === '/login' && authenticated) {
    return '/boards'
  }

  return true
})
export default router
