# Super AI Agent Frontend

Vue 3 + Vite 前端项目，包含两个流式对话应用：

- `AI 宠物咨询`：`/pet`，调用 `GET /api/ai/pet_app/chat/sse`
- `AI 超级智能体`：`/agent`，调用 `GET /api/ai/manus/chat`

## 开发

```bash
npm install
npm run dev
```

默认使用 Vite 代理：`/api -> http://localhost:8123`。

## 页面说明

- `/`：深色科技风主页，应用切换按钮支持缩放、扫光与发光动效。
- `/pet`：宠物主题聊天室，自动生成 `chatId`，SSE 打字机式流式显示，自动滚动到底部。
- `/agent`：科技机甲风聊天室，SSE 流式输出与科技感消息样式。
