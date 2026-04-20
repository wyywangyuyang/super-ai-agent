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
const conversations = ref([])
const activeConversationId = ref('')
const sidebarOpen = ref(false)
let source = null

onMounted(() => {
  startNewChat()
})

onBeforeUnmount(() => {
  closeSseStream(source)
})

const statusText = computed(() => {
  if (isStreaming.value) return '连接中...'
  if (errorText.value) return `异常: ${errorText.value}`
  return '就绪'
})

function formatTime(ts) {
  if (!ts) return ''
  const d = new Date(ts)
  const h = String(d.getHours()).padStart(2, '0')
  const m = String(d.getMinutes()).padStart(2, '0')
  return `${h}:${m}`
}

function normalizeConversationOrder() {
  const current = conversations.value.find((c) => c.title === '当前对话')
  const draft = conversations.value.find((c) => c.title === '新对话')
  const history = conversations.value
    .filter((c) => c.title !== '当前对话' && c.title !== '新对话')
    .sort((a, b) => (b.lastActiveAt || b.createdAt || 0) - (a.lastActiveAt || a.createdAt || 0))

  const ordered = []
  if (draft) ordered.push(draft)
  if (current) ordered.push(current)
  ordered.push(...history)
  conversations.value = ordered
}

function startNewChat() {
  closeSseStream(source)
  isStreaming.value = false
  errorText.value = ''
  inputText.value = ''
  messages.value = []
  chatId.value = createChatId()

  conversations.value = conversations.value.map((item) => {
    if (item.title === '当前对话') return { ...item, title: '历史对话' }
    return item
  })

  const now = Date.now()
  const draftId = `draft-${now}`
  const currentId = `session-${now + 1}`

  conversations.value.push({ id: currentId, title: '当前对话', createdAt: now, lastActiveAt: now })

  activeConversationId.value = currentId
  normalizeConversationOrder()
  sidebarOpen.value = false
}

function pickConversation(id) {
  activeConversationId.value = id
  sidebarOpen.value = false
}

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

  if (conversations.value.length && activeConversationId.value) {
    const idx = conversations.value.findIndex((c) => c.id === activeConversationId.value)
    if (idx >= 0) {
      conversations.value[idx].lastActiveAt = Date.now()
      if (
        conversations.value[idx].title === '新对话' ||
        conversations.value[idx].title === '当前对话' ||
        conversations.value[idx].title === '历史对话'
      ) {
        conversations.value[idx].title = content.slice(0, 18)
      }
    }
  }
  normalizeConversationOrder()

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
  <section class="chat-page pet-theme fade-in chat-layout">
    <aside :class="['chat-sidebar', sidebarOpen ? 'open' : '']">
      <button class="new-chat-btn" @click="startNewChat">新对话</button>
      <div class="conversation-list">
        <button
          v-for="item in conversations"
          :key="item.id"
          :class="['conversation-item', item.id === activeConversationId ? 'active' : '']"
          @click="pickConversation(item.id)"
        >
          <span class="conversation-title">{{ item.title }}</span>
          <span class="conversation-time">{{ formatTime(item.lastActiveAt || item.createdAt) }}</span>
        </button>
      </div>
    </aside>

    <div class="chat-main">
      <button class="mobile-sidebar-btn" @click="sidebarOpen = !sidebarOpen">会话</button>
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
    </div>
  </section>
</template>

<style scoped>
.chat-layout {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 0;
  min-height: 100vh;
}

.chat-sidebar {
  border-right: 1px solid rgba(148, 163, 184, 0.25);
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.new-chat-btn,
.conversation-item,
.mobile-sidebar-btn {
  border: 1px solid rgba(77, 124, 240, 0.28);
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.65);
  color: #2d5fe8;
  padding: 10px 12px;
  text-align: left;
  cursor: pointer;
}

.conversation-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  overflow-y: auto;
}

.conversation-item.active {
  background: rgba(76, 124, 240, 0.12);
}

.conversation-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.conversation-title {
  font-size: 14px;
  line-height: 1.3;
}

.conversation-time {
  font-size: 12px;
  opacity: 0.75;
}

.chat-main {
  min-width: 0;
  padding: 12px;
}

.mobile-sidebar-btn {
  display: none;
  margin-bottom: 8px;
}

.messages {
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
  gap: 8px;
  align-items: flex-start;
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

.ai-bubble {
  background: rgba(255, 255, 255, 0.92);
  color: #1f2937;
  border: 1px solid rgba(15, 23, 42, 0.08);
}

.pet-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, #10b981, #059669);
  color: #fff;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 4px;
}

@media (max-width: 900px) {
  .chat-layout {
    grid-template-columns: 1fr;
  }

  .chat-sidebar {
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    width: 240px;
    transform: translateX(-100%);
    transition: transform 0.2s ease;
    z-index: 20;
    background: inherit;
  }

  .chat-sidebar.open {
    transform: translateX(0);
  }

  .mobile-sidebar-btn {
    display: inline-block;
  }
}
</style>
