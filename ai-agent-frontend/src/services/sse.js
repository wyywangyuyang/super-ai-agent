export function createSseStream(url, onData, onDone, onError) {
  const eventSource = new EventSource(url)

  eventSource.onmessage = (event) => {
    const value = event.data || ''
    if (value === '[DONE]') {
      onDone?.()
      eventSource.close()
      return
    }
    onData?.(value)
  }

  eventSource.onerror = () => {
    onError?.(new Error('SSE connection failed'))
    eventSource.close()
  }

  return eventSource
}

export function closeSseStream(source) {
  if (source && source.readyState !== EventSource.CLOSED) {
    source.close()
  }
}
