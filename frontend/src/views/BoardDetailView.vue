<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getBoard, updateBoard, deleteBoard } from '../api/board'
import { getComments, createComment, updateComment, deleteComment } from '../api/comment'
import { getCurrentUsername,isAdmin} from '../utils/auth'

const route = useRoute()
const router = useRouter()

const board = ref(null)

const editingBoard = ref(false)
const editTitle = ref('')
const editContent = ref('')

const comments = ref([])
const commentLoading = ref(false)
const commentError = ref('')

const commentContent = ref('')
const commentSubmitting = ref(false)

const editingCommentId = ref(null)
const editingContent = ref('')

const loading = ref(false)
const errorMessage = ref('')

const currentUsername = getCurrentUsername()
const admin = isAdmin()

const loadBoard = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const boardId = route.params.boardId
    const response = await getBoard(boardId)
    console.log('게시글 상세 응답:', response.data)

    board.value = response.data.data
  } catch (error) {
    console.error('게시글 상세 조회 실패:', error)
    errorMessage.value = '게시글을 불러오지 못했습니다.'
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  commentLoading.value = true
  commentError.value = ''

  try {
    const boardId = route.params.boardId
    const response = await getComments(boardId)
    console.log('댓글 응답:', response.data)
    comments.value = response.data.data

  } catch (error) {
    console.error('댓글 조회 실패:', error)
    commentError.value = '댓글을 불러오지 못했습니다.'

  } finally {
    commentLoading.value = false
  }
}

const submitComment = async () => {
  const content = commentContent.value.trim()

  if (!content) {
    alert('댓글 내용을 입력해주세요.')
    return
  }

  commentSubmitting.value = true

  try {
    const boardId = route.params.boardId

    await createComment(boardId, {
      content: content,
    })

    commentContent.value = ''
    await loadComments()

  } catch (error) {
    console.error('댓글 작성 실패:', error)
  } finally {
    commentSubmitting.value = false
  }
}

const startEditComment = (comment) => {
  editingCommentId.value = comment.id
  editingContent.value = comment.content
}

const cancelEditComment = () => {
  editingCommentId.value = null
  editingContent.value = ''
}

const submitEditComment = async () => {
  const content = editingContent.value.trim()

  if (!content) {
    alert('댓글 내용을 입력해주세요.')
    return
  }

  try {
    const boardId = route.params.boardId
    const commentId = editingCommentId.value

    await updateComment(boardId, commentId, {
      content: content,
    })

    cancelEditComment()

    await loadComments()
  } catch (error) {
    console.error('댓글 수정 실패:', error)
  }
}

const removeComment = async (commentId) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) {
    return
  }

  try {
    const boardId = route.params.boardId
    await deleteComment(boardId, commentId)
    await loadComments()

  } catch (error) {
    console.error('댓글 삭제 실패:', error)
  }
}

const startEditBoard = () => {
  editTitle.value = board.value.title
  editContent.value = board.value.content
  editingBoard.value = true
}

const cancelEditBoard = () => {
  editingBoard.value = false
  editTitle.value = ''
  editContent.value = ''
}

const submitEditBoard = async () => {
  const title = editTitle.value.trim()
  const content = editContent.value.trim()

  if (!title) {
    alert('제목을 입력해주세요.')
    return
  }

  if (!content) {
    alert('내용을 입력해주세요.')
    return
  }

  try {
    const boardId = route.params.boardId

    await updateBoard(boardId, { title,content, })
    editingBoard.value = false
    await loadBoard()

  } catch (error) {
    console.error('게시글 수정 실패:', error)
  }
}

const removeBoard = async () => {
  if (!confirm('게시글을 삭제하시겠습니까?')) {
    return
  }

  try {
    const boardId = route.params.boardId

    await deleteBoard(boardId)

    router.push('/boards')
  } catch (error) {
    console.error('게시글 삭제 실패:', error)
  }
}

const canModifyBoard = () => {
  if (!board.value) {
    return false
  }

  return (
    admin ||
    board.value.writer === currentUsername
  )
}

const canModifyComment = (comment) => {
  return (
    admin ||
    comment.writer === currentUsername
  )
}

const goToList = () => {
  router.push('/boards')
}

onMounted(async () => {
  await loadBoard()
  await loadComments()
})
</script>

<template>
  <div>
    <h1>게시글 상세</h1>

    <p v-if="loading">
      게시글을 불러오는 중입니다...
    </p>

    <p v-if="errorMessage">
      {{ errorMessage }}
    </p>

  <div v-if="board && !loading">

    <!-- 일반 보기 -->
    <div v-if="!editingBoard">
      <h2>{{ board.title }}</h2>

      <div>       작성자: {{ board.username }}  </div>
      <div>       조회수: {{ board.viewCount }} </div>
      <hr />

      <div> {{ board.content }}  </div>
      <br />

      <button v-if="canModifyBoard()" type="button" @click="startEditBoard"  >수정 </button>
      <button v-if="canModifyBoard()" type="button" @click="removeBoard"> 삭제 </button>
    </div>

    <!-- 수정 모드 -->
    <div v-else>
      <input v-model="editTitle" type="text" />
      <textarea v-model="editContent" rows="10"></textarea>
      <button type="button" @click="submitEditBoard">수정 완료</button>
      <button type="button" @click="cancelEditBoard"> 취소</button>
    </div>
  </div>

    <button type="button" @click="goToList"> 목록 </button>
    <hr />

    <h3>댓글</h3>

    <p v-if="commentLoading">
      댓글을 불러오는 중입니다...
    </p>

    <p v-if="commentError">
      {{ commentError }}
    </p>

    <p v-if="!commentLoading && comments.length === 0">
      등록된 댓글이 없습니다.
    </p>

    <div
      v-for="comment in comments"
      :key="comment.id"
    >
      <div>
        <strong>{{ comment.writer }}</strong>
      </div>

      <!-- 일반 상태 -->
      <div v-if="editingCommentId !== comment.id">
        <p>{{ comment.content }}</p>

         <button
           v-if="canModifyComment(comment)"
           type="button"
           @click="startEditComment(comment)"
         >
           수정
         </button>

         <button
           v-if="canModifyComment(comment)"
           type="button"
           @click="removeComment(comment.id)"
         >
           삭제
         </button>
      </div>

      <!-- 수정 상태 -->
      <div v-else>
        <textarea
          v-model="editingContent"
          rows="4"
        ></textarea>

        <button
          type="button"
          @click="submitEditComment"
        >
          수정 완료
        </button>

        <button
          type="button"
          @click="cancelEditComment"
        >
          취소
        </button>
      </div>

      <hr />
    </div>

    <div class="comment-form">
      <textarea v-model="commentContent" rows="4" placeholder="댓글을 입력해주세요."
      ></textarea>

      <button type="button" :disabled="commentSubmitting" @click="submitComment">
        {{ commentSubmitting ? '등록 중...' : '댓글 등록' }}
      </button>
    </div>

  </div>
</template>
