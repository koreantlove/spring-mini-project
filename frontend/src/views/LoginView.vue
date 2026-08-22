<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '../api/axios'

const username = ref('')
const password = ref('')
const message = ref('')

const route = useRoute()
const router = useRouter()

const login = async () => {
  try {
    const response = await api.post('/api/users/login', {
      username: username.value,
      password: password.value,
    })

    console.log('login response:', response.data)
    const token = response.data.data.accessToken

    localStorage.setItem('accessToken', token)
    message.value = '로그인 성공'

    const redirect = route.query.redirect || '/boards'
    router.push(redirect)

  } catch (error) {
    console.error(error)

    message.value = '로그인 실패'
  }
}
</script>

<template>
  <main>
    <h1>로그인</h1>

    <div>
      <label>아이디</label>
      <input
        v-model="username"
        type="text"
      />
    </div>

    <div>
      <label>비밀번호</label>
      <input
        v-model="password"
        type="password"
      />
    </div>

    <button @click="login">
      로그인
    </button>

    <p>{{ message }}</p>
  </main>
</template>
