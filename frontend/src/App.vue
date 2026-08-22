<script setup>
import { getCurrentUsername, logout } from './utils/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = getCurrentUsername()

const handleLogout = () => {
  logout()
  router.push('/login')
}
</script>

<template>

   <header>
      <span v-if="username">
        {{ username }}님
      </span>

      <button
        v-if="username"
        type="button"
        @click="handleLogout"
      >
        로그아웃
      </button>
    </header>

  <router-view />
</template>
