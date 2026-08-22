<script setup>
import { onMounted, ref } from 'vue'
import { getBoards } from '../api/board'

const boards = ref([])

const loading = ref(false)
const errorMessage = ref('')

const currentPage = ref(0)
const pageSize = ref(10)
const totalPages = ref(0)
const totalElements = ref(0)

const searchType = ref('title')
const keyword = ref('')

const loadBoards = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await getBoards({
      page: currentPage.value,
      size: pageSize.value,
      type: searchType.value,
      keyword: keyword.value,
    })

    const data = response.data.data

    boards.value = data.content
    totalPages.value = data.totalPages
    totalElements.value = data.totalElements

  } catch (error) {
    console.error('게시글 조회 실패:', error)
    errorMessage.value = '게시글을 불러오지 못했습니다.'

  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBoards()
})

const goToPage = (page) => {
  if (page < 0 || page >= totalPages.value) {
    return
  }

  currentPage.value = page
  loadBoards()
}

const search = async () => {
  console.log('검색 실행')
  console.log('검색 타입:', searchType.value)
  console.log('검색어:', keyword.value)

  currentPage.value = 0

  await loadBoards()
}

</script>



<template>
  <div>
    <h1>게시글</h1>

    <p v-if="loading">
      게시글을 불러오는 중입니다...
    </p>

    <p v-if="errorMessage">
      {{ errorMessage }}
    </p>

    <div class="search-area">
      <select v-model="searchType">
        <option value="title">제목</option>
        <option value="content">내용</option>
        <option value="writer">작성자</option>
      </select>

      <input
        v-model="keyword"
        type="text"
        placeholder="검색어를 입력하세요"
      />

      <button
        type="button"
        @click="search"
      >
        검색
      </button>
    </div>

    <table v-if="!loading && boards.length > 0">
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>조회수</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="board in boards"
          :key="board.boardId"
        >
          <td>{{ board.id }}</td>
          <td>
            <RouterLink :to="`/boards/${board.id}`">
              {{ board.title }}
            </RouterLink>
          </td>
          <td>{{ board.writer }}</td>
          <td>{{ board.viewCount }}</td>
        </tr>
      </tbody>
    </table>

    <p v-if="!loading && boards.length === 0">
      게시글이 없습니다.
    </p>
  </div>

  <div v-if="totalPages > 0">
    <button :disabled="currentPage === 0"  @click="goToPage(currentPage - 1)">
      이전
    </button>

    <button v-for="page in totalPages" :key="page" :disabled="currentPage === page - 1"
      @click="goToPage(page - 1)"
    >
      {{ page }}
    </button>

    <button :disabled="currentPage === totalPages - 1" @click="goToPage(currentPage + 1)"
    >
      다음
    </button>
  </div>

</template>
