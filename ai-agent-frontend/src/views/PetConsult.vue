<script setup>
import { computed, onBeforeUnmount, onMounted, ref } from 'vue'
import TechPanel from '../components/TechPanel.vue'
import TypewriterText from '../components/TypewriterText.vue'
import { createSseStream, closeSseStream } from '../services/sse'
import { createAiMessage, createChatId, scrollToBottom } from '../utils/chat'

const chatId = ref('')
const inputText = ref('')
const isStreaming = ref(false)
const errorText = ref('')
const messagesRef = ref(null)
const messages = ref([])
let source = null

onMounted(() => {
  chatId.value = createChatId()
})

onBeforeUnmount(() => {
  closeSseStream(source)
})

const statusText = computed(() => {
  if (isStreaming.value) return '连接中...'
  if (errorText.value) return `异常: ${errorText.value}`
  return '就绪'
})

function appendUserMessage(content) {
  messages.value.push({
    id: `user-${Date.now()}`,
    role: 'user',
    content,
    time: Date.now(),
  })
}

function sendMessage() {
  const content = inputText.value.trim()
  if (!content || isStreaming.value) return

  errorText.value = ''
  appendUserMessage(content)
  inputText.value = ''

  const aiMessage = createAiMessage('pet')
  messages.value.push(aiMessage)
  isStreaming.value = true
  scrollToBottom(messagesRef)

  closeSseStream(source)
  const query = new URLSearchParams({ message: content, chatId: chatId.value }).toString()

  source = createSseStream(
    `/api/ai/pet_app/chat/sse?${query}`,
    (chunk) => {
      aiMessage.content += chunk
      messages.value = [...messages.value]
      scrollToBottom(messagesRef)
    },
    () => {
      isStreaming.value = false
      scrollToBottom(messagesRef)
    },
    (err) => {
      isStreaming.value = false
      errorText.value = err.message
      scrollToBottom(messagesRef)
    },
  )
}
</script>

<template>
  <section class="chat-page pet-theme fade-in">
    <TechPanel title="AI 宠物咨询聊天室">
      <div class="chat-meta">
        <span>聊天室 ID: {{ chatId }}</span>
        <span class="status">状态: {{ statusText }}</span>
      </div>

      <div ref="messagesRef" class="messages">
        <article
          v-for="item in messages"
          :key="item.id"
          :class="['bubble-row', item.role === 'user' ? 'user-row' : 'ai-row']"
        >
          <div v-if="item.role === 'assistant'" class="pet-avatar">PET</div>
          <div :class="['bubble', item.role === 'user' ? 'user-bubble' : 'ai-bubble']">
            <TypewriterText v-if="item.role === 'assistant'" :text="item.content" :speed="14" />
            <span v-else>{{ item.content }}</span>
          </div>
        </article>
      </div>

      <div class="input-bar">
        <input
          v-model="inputText"
          type="text"
          class="chat-input"
          placeholder="和宠物 AI 说点什么..."
          @keydown.enter="sendMessage"
        />
        <button class="send-btn glow-btn" :disabled="isStreaming" @click="sendMessage">发送</button>
      </div>
    </TechPanel>
  </section>
</template>
