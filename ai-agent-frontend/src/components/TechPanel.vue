<template>
  <section class="tech-panel">
    <header v-if="title" class="panel-title">{{ title }}</header>
    <div class="panel-content">
      <slot />
    </div>
  </section>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: '',
  },
})
</script>

<style scoped>
.tech-panel {
  position: relative;
  border-radius: 18px;
  border: 1px solid rgba(126, 143, 255, 0.3);
  background: linear-gradient(160deg, rgba(17, 22, 40, 0.9), rgba(27, 30, 52, 0.65));
  backdrop-filter: blur(12px);
  overflow: hidden;
}

.tech-panel::before {
  content: '';
  position: absolute;
  inset: 0;
  pointer-events: none;
  background: linear-gradient(120deg, transparent 20%, rgba(80, 177, 255, 0.1), transparent 80%);
}

.panel-title {
  padding: 14px 18px;
  border-bottom: 1px solid rgba(126, 143, 255, 0.22);
  color: #b6c2ff;
  font-size: 14px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.panel-content {
  position: relative;
  padding: 18px;
}
</style>
