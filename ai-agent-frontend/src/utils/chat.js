export function createChatId() {
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return crypto.randomUUID()
  }
  return `chat-${Date.now()}-${Math.random().toString(16).slice(2)}`
}

export function scrollToBottom(containerRef) {
  if (!containerRef?.value) return
  requestAnimationFrame(() => {
    containerRef.value.scrollTop = containerRef.value.scrollHeight
  })
}

export function createAiMessage(theme = 'pet') {
  return {
    id: `${theme}-${Date.now()}-${Math.random().toString(16).slice(2)}`,
    role: 'assistant',
    content: '',
    time: Date.now(),
  }
}
