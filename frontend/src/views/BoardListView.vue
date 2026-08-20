<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api/axios'

import { computed } from 'vue'
import { getRole } from '../utils/jwt'

const boards = ref([])
const error = ref('')

const router = useRouter()

const role = computed(() => getRole())

const getBoards = async () => {
  try {
    const response = await api.get('/api/boards')

    console.log('board response:', response.data)

    boards.value = response.data.data.content
  } catch (error) {
    console.error(error)

    error.value = '게시글 조회에 실패했습니다.'
  }
}

const logout = () => {
  localStorage.removeItem('accessToken')

  router.push('/login')
}


onMounted(() => {
  getBoards()
})

</script>

<template>
  <main>
    <h1>게시글 목록</h1>
    <p>
      현재 권한: {{ role }}
    </p>
    <button @click="logout"> 로그아웃 </button>

    <p v-if="error"> {{ error }} </p>

    <ul>
      <li v-for="board in boards" :key="board.id" >
        {{ board.title }}
      </li>
    </ul>
  </main>
</template>
