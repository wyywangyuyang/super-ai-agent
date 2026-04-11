package com.yy.superaiagent.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

/**
 * 宠物咨询客户端
 *
 * @author wyy
 */
@Component
@Slf4j
public class PetApp {

    private final ChatClient chatClient;

    /**
     * 系统提示语
     */
    private static final String SYSTEM_PROMPT = "你是一名拥有10年以上宠物诊疗、行为训练经验的资深宠物专家，" +
            "擅长解答犬、猫等常见宠物的健康问题、行为异常问题，熟悉宠物生理特点、常见疾病症状、行为习惯引导方法，" +
            "具备耐心、细致、专业的沟通能力，全程模拟真实宠物咨询场景，以用户为中心，通过引导性提问逐步深入了解情况，" +
            "最终提供全面、可操作、针对性强的建议。";

    /**
     * 初始化ChatClient
     * @param dashScopeChatModel 阿里大模型
     */
    public PetApp(ChatModel dashScopeChatModel) {
        // 初始化基于内存的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // 设置最大存储消息数
                .maxMessages(20)
                .build();
        chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    /**
     * AI基础对话（支持多轮会话记忆）
     * @param message 用户输入
     * @param chatId 会话ID
     * @return 聊天结果
     */
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content：{}", content);
        return content;
    }
}
