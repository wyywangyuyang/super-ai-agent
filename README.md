

# AI-Love-Agent

基于 Spring AI + DashScope 构建的智能宠物助手应用。

## 项目简介

AI-Love-Agent 是一个智能宠物助手机器人应用，基于 Spring AI 框架和阿里云DashScope 大语言模型构建。该应用提供了简洁的聊天接口，允许用户与 AI 宠物进行自然语言交互。

## 技术栈

- **Spring Boot** - Spring 生态系统
- **Spring AI** - AI 集成框架
- **DashScope (阿里云)** - 大语言模型服务
- **Maven** - 项目构建工具

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+

### 配置步骤

1. 克隆项目：
```bash
git clone https://gitee.com/wyy000000000/ai-love-agent.git
cd ai-love-agent
```

2. 配置阿里云 DashScope API Key：
在 `src/main/resources/application.yml` 中配置您的 API Key：

```yaml
spring:
  ai:
    dashscope:
      api-key: your-api-key
```

3. 构建并运行：
```bash
./mvnw spring-boot:run
```

### 接口访问

- 健康检查：`http://localhost:8080/health`
- 应用默认端口：8080

## 核心功能

### PetApp - 宠物聊天

`PetApp` 是核心聊天组件，提供与 AI 宠物的对话功能：

```java
@Autowired
private PetApp petApp;

String response = petApp.doChat("你好", "chat-001");
```

### HealthController - 健康检查

提供基础的健康检查接口，用于服务监控。

## 项目结构

```
src/
├── main/
│   ├── java/com/yy/superaiagent/
│   │   ├── SuperAiAgentApplication.java  # Spring Boot 启动类
│   │   ├── app/
│   │   │   └── PetApp.java               # 宠物聊天组件
│   │   ├── controller/
│   │   │   └── HealthController.java     # 健康检查控制器
│   │   └── demo/invoke/
│   │       └── SpringAiAiInvoke.java      # AI 调用示例
│   └── resources/
│       └── application.yml                # 应用配置
└── test/
    └── java/...                           # 测试类
```

## 许可证

本项目仅供学习交流使用。