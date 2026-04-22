# Super AI Agent (包含分身：AI 宠物咨询应用与 AI 超级智能体)

Super AI Agent 是一个全栈 AI 平台，结合了现代化的 Vue 3 前端和强大的基于 Spring Boot 3 & Spring AI Alibaba 的后端环境。本项目不仅搭建了底层框架，更直接落地了两个核心 AI 业务应用，充分展示了大语言模型在垂直领域检索（RAG）与复杂任务编排（Agent+MCP）上的强大能力。

## 🎯 核心业务应用

### 1. AI 宠物咨询应用（基于 RAG 知识库检索）
专注解答宠物喂养的各类疑难问题。系统底层加载了《新手养猫指南》等专业文档资产，基于检索增强生成（RAG）技术进行本地高精度知识检索问答，提供专业的宠物饲养、疾病预防、日常护理等定制化咨询反馈，有效消除通用大模型的答非所问与“幻觉”。

### 2. AI 超级智能体应用（基于 Agent Planner、Tool Calling 与 MCP）
具备多步推理规划与外部工具调用能力的全能型助手。超级智能体不仅能深入理解用户意图并制定多步执行计划（例如：策划一场完整的约会行程规划），还能通过 Tool Calling（工具调用）机制触发真实的业务逻辑，同时结合 MCP 服务调用图像搜索等外部工具，实时检索并返回图片资源以丰富回答内容。这使得大模型从单纯的“聊天机器”进阶为能执行实际任务操作的“超级助手”。

## 🌟 三大核心技术亮点

### 1. RAG (检索增强生成)
- 包含文档读取、分块处理和向量存储模块。
- 结合大语言模型与本地私库知识，打通企业/个人私有数据的智能问答，并提供精准的知识回溯源。

### 2. Tool Calling (工具调用)
- 破除大模“只会说不会做”的局限。
- 智能体可直接利用 Tool Calling 机制自主调用 Java 后端定义好的 Function（如本地文件处理、规划服务等），大幅提升了 AI 完成复杂任务的能力。

### 3. MCP (Model Context Protocol 模型上下文协议)
- **上下文与外部能力拓展**：基于标准化的模型上下文协议（MCP），本项目独立实现并接入了图像搜索等外部聚合数据服务。
- 实现了大模型跨系统的数据整合与行动控制的解耦。

## 🏗️ 基础架构特性

### 1. 后端服务 (Backend - Spring Boot)
- **大模型集成**：基于 Spring AI Alibaba 深度接入阿里云通义千问大模型（DashScope），实现基础的对话与逻辑处理。
- **状态与记忆持久化**：使用 Kryo 等工具实现多轮对话上下文交互日志及智能体状态的记忆持久化存储。

### 2. 前端界面 (Frontend - Vue 3)
- **智能对话界面**：采用响应式的 UI 设计（Vue 3 + Vite），支持流式文本输出，提供流畅自然的人机交互体验。
- **Agent 交互可视化**：提供清晰直观的聊天面板，便于用户发起检索、规划或各类定制化任务请求。

## 📁 项目结构

- **后端 (`/`)**: 位于根目录的 Spring Boot 3.5.13 Java 应用。
- **前端 (`/ai-agent-frontend`)**: 独立的现代 Web 前端工程。
- **MCP 服务 (`/image-search-mcp-server`)**: 独立的 MCP 协议接口模块。

## 🛠️ 环境依赖

- JDK 21+
- Node.js & npm (用于前端)
- Maven
- 有效的 DashScope API Key (需在 `application.yml` 或环境变量中配置)

## 🚀 快速启动

### 1. 后端服务启动

```bash
# 编译项目
./mvnw clean install

# 运行后端应用
./mvnw spring-boot:run
```

### 2. 前端服务启动

```bash
cd ai-agent-frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

### 3. MCP 服务启动

```bash
cd image-search-mcp-server
./mvnw clean install
./mvnw spring-boot:run
```

## 🏗️ 技术栈

- **后端**: Java 21, Spring Boot 3.5.13, Spring AI Alibaba, DashScope, Hutool, Kryo
- **前端**: Vue 3, Vite
- **API 文档**: Knife4j OpenAPI 3
