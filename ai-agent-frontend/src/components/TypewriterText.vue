<script setup>
import { computed, ref, watch } from 'vue'

const props = defineProps({
  text: {
    type: String,
    default: '',
  },
  speed: {
    type: Number,
    default: 16,
  },
})

const shownLength = ref(0)
let timer = null

watch(
  () => props.text,
  (newText) => {
    if (!newText) {
      shownLength.value = 0
      return
    }

    if (timer) {
      clearInterval(timer)
    }

    timer = setInterval(() => {
      if (shownLength.value < newText.length) {
        shownLength.value += 1
      } else {
        clearInterval(timer)
        timer = null
      }
    }, props.speed)
  },
  { immediate: true },
)

const displayText = computed(() => props.text.slice(0, shownLength.value))
</script>

<template>
  <span>{{ displayText }}</span>
</template>

