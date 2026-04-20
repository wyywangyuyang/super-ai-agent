import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8123',
        // target: 'https://ai-agent-backend-248800-6-1424056544.sh.run.tcloudbase.com',
        changeOrigin: true,
      },
    },
  },
})
