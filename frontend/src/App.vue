<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { accessToken, getCurrentUsername, isAdmin, isAuthenticated, logout,} from './utils/auth'

const router = useRouter()

const authenticated = computed(() => {
  return isAuthenticated()
})

const username = computed(() => {
  return authenticated.value
    ? getCurrentUsername()
    : null
})

const admin = computed(() => {
  return authenticated.value
    ? isAdmin()
    : false
})

const handleLogout = () => {
  logout()
  router.push('/login')
}
</script>

<template>
  <header class="app-header">
    <div class="header-inner">

      <div class="logo">
        <RouterLink to="/boards">
          Board
        </RouterLink>
      </div>

      <nav>
        <RouterLink v-if="authenticated" to="/boards" >게시글</RouterLink>
        <span v-if="admin" class="admin-menu" >관리자</span>
      </nav>

      <div class="user-area">
        <template v-if="authenticated">
          <span>
            {{ username }}님
          </span>

          <span v-if="admin">
            (ADMIN)
          </span>

          <button
            type="button"
            @click="handleLogout"
          >
            로그아웃
          </button>

        </template>

        <RouterLink
          v-else
          to="/login"
        >
          로그인
        </RouterLink>

      </div>

    </div>
  </header>

  <main>
    <router-view />
  </main>
</template>
