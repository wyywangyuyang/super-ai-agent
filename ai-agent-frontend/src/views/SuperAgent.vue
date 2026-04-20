<script setup>
import { computed, onBeforeUnmount, ref } from 'vue'
import TechPanel from '../components/TechPanel.vue'
import TypewriterText from '../components/TypewriterText.vue'
import { createSseStream, closeSseStream } from '../services/sse'
import { scrollToBottom } from '../utils/chat'

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

  isStreaming.value = true
  scrollToBottom(messagesRef)

  closeSseStream(source)
  const query = new URLSearchParams({ message: content }).toString()
  let stepIndex = 0

  source = createSseStream(
    `/api/ai/manus/chat?${query}`,
    (chunk) => {
      const stepContent = String(chunk ?? '').trim()
      if (!stepContent) return

      stepIndex += 1
      const displayText = /^step\s*\d+\s*:/i.test(stepContent)
        ? stepContent
        : `Step ${stepIndex}: ${stepContent}`

      messages.value.push({
        id: `assistant-${Date.now()}-${stepIndex}`,
        role: 'assistant',
        content: displayText,
      })
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
        <span>模式: AI超级智能体</span>
        <span class="status">状态: {{ statusText }}</span>
      </div>

      <div ref="messagesRef" class="messages tech-messages">
        <article
          v-for="item in messages"
          :key="item.id"
          :class="['bubble-row', item.role === 'user' ? 'user-row' : 'ai-row']"
        >
          <div :class="['bubble', item.role === 'user' ? 'user-bubble' : 'agent-bubble']">
            <span>{{ item.content ?? item.text }}</span>
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

<style scoped>
.messages.tech-messages {
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 62vh;
  overflow-y: auto;
  padding: 8px 6px 12px;
}

.bubble-row {
  display: flex;
  width: 100%;
}

.user-row {
  justify-content: flex-end;
}

.ai-row {
  justify-content: flex-start;
}

.bubble {
  max-width: min(78%, 920px);
  white-space: pre-wrap;
  word-break: break-word;
  line-height: 1.6;
  border-radius: 14px;
  padding: 12px 14px;
}

.user-bubble {
  background: linear-gradient(135deg, #4b7cf0, #4f8cff);
  color: #fff;
}

.agent-bubble {
  background: rgba(255, 255, 255, 0.92);
  color: #1f2937;
  border: 1px solid rgba(15, 23, 42, 0.08);
}
</style>
