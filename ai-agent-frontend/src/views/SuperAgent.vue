<script setup>
import { computed, onBeforeUnmount, ref } from 'vue'
import TechPanel from '../components/TechPanel.vue'
import TypewriterText from '../components/TypewriterText.vue'
import { createSseStream, closeSseStream } from '../services/sse'
import { createAiMessage, scrollToBottom } from '../utils/chat'

const inputText = ref('')
const isStreaming = ref(false)
const errorText = ref('')
const messagesRef = ref(null)
const messages = ref([])
let source = null

onBeforeUnmount(() => {
  closeSseStream(source)
})

const statusText = computed(() => {
  if (isStreaming.value) return '智能体处理中...'
  if (errorText.value) return `异常: ${errorText.value}`
  return '待命'
})

function sendMessage() {
  const content = inputText.value.trim()
  if (!content || isStreaming.value) return

  errorText.value = ''
  messages.value.push({
    id: `user-${Date.now()}`,
    role: 'user',
    content,
  })
  inputText.value = ''

  const aiMessage = createAiMessage('agent')
  messages.value.push(aiMessage)
  isStreaming.value = true
  scrollToBottom(messagesRef)

  closeSseStream(source)
  const query = new URLSearchParams({ message: content }).toString()

  source = createSseStream(
    `/api/ai/manus/chat?${query}`,
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
  <section class="chat-page agent-theme fade-in">
    <TechPanel title="AI 超级智能体">
      <div class="chat-meta">
        <span>模式: 未来机甲智能体</span>
        <span class="status">状态: {{ statusText }}</span>
      </div>

      <div ref="messagesRef" class="messages tech-messages">
        <article
          v-for="item in messages"
          :key="item.id"
          :class="['bubble-row', item.role === 'user' ? 'user-row' : 'ai-row']"
        >
          <div :class="['bubble', item.role === 'user' ? 'user-bubble' : 'agent-bubble']">
            <TypewriterText v-if="item.role === 'assistant'" :text="item.content" :speed="10" />
            <span v-else>{{ item.content }}</span>
          </div>
        </article>
      </div>

      <div class="input-bar">
        <input
          v-model="inputText"
          type="text"
          class="chat-input"
          placeholder="向超级智能体输入任务..."
          @keydown.enter="sendMessage"
        />
        <button class="send-btn glow-btn" :disabled="isStreaming" @click="sendMessage">执行</button>
      </div>
    </TechPanel>
  </section>
</template>

